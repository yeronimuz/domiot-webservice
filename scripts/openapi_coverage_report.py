#!/usr/bin/env python3
"""Generate an API coverage report by comparing contract interfaces and live OpenAPI docs."""

from __future__ import annotations

import argparse
import json
import os
import re
import sys
import urllib.error
import urllib.request
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Iterable, List, Set, Tuple


MAPPING_PATTERN = re.compile(
    r'@RequestMapping\(\s*method\s*=\s*RequestMethod\.(\w+)\s*,\s*value\s*=\s*"([^"]+)"',
    re.S,
)
INTERFACE_PATTERN = re.compile(r"interface\s+(\w+)")


@dataclass(frozen=True, order=True)
class Endpoint:
    method: str
    path: str


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--api-sources",
        required=True,
        help="Path to domiot-rest-api sources JAR",
    )
    parser.add_argument(
        "--openapi-url",
        default="http://localhost:7010/api/v3/api-docs",
        help="OpenAPI JSON endpoint URL",
    )
    parser.add_argument(
        "--out-dir",
        default="build/reports/api-coverage",
        help="Directory where report files are written",
    )
    return parser.parse_args()


def load_expected_endpoints(source_jar: Path) -> Tuple[Set[Endpoint], Dict[str, List[Endpoint]]]:
    if not source_jar.exists():
        raise FileNotFoundError(f"API source jar not found: {source_jar}")

    expected: Set[Endpoint] = set()
    by_interface: Dict[str, List[Endpoint]] = {}

    with zipfile.ZipFile(source_jar) as zf:
        api_sources = [name for name in zf.namelist() if name.endswith("Api.java")]
        for source_name in sorted(api_sources):
            source_code = zf.read(source_name).decode("utf-8", errors="replace")
            interface_match = INTERFACE_PATTERN.search(source_code)
            interface_name = interface_match.group(1) if interface_match else source_name

            interface_endpoints: List[Endpoint] = []
            for method, path in MAPPING_PATTERN.findall(source_code):
                endpoint = Endpoint(method=method.upper(), path=path)
                interface_endpoints.append(endpoint)
                expected.add(endpoint)

            by_interface[interface_name] = sorted(interface_endpoints)

    return expected, by_interface


def load_actual_endpoints(openapi_url: str) -> Set[Endpoint]:
    try:
        with urllib.request.urlopen(openapi_url, timeout=15) as response:
            payload = json.load(response)
    except urllib.error.URLError as exc:
        raise RuntimeError(f"Failed to fetch OpenAPI JSON from {openapi_url}: {exc}") from exc

    actual: Set[Endpoint] = set()
    for path, methods in payload.get("paths", {}).items():
        for method in methods.keys():
            actual.add(Endpoint(method=method.upper(), path=path))
    return actual


def write_report(
    out_dir: Path,
    openapi_url: str,
    expected: Set[Endpoint],
    actual: Set[Endpoint],
    by_interface: Dict[str, List[Endpoint]],
) -> int:
    out_dir.mkdir(parents=True, exist_ok=True)

    missing = sorted(expected - actual)
    extra = sorted(actual - expected)
    matched = sorted(expected & actual)

    report_json = {
        "openapiUrl": openapi_url,
        "expectedCount": len(expected),
        "actualCount": len(actual),
        "matchedCount": len(matched),
        "missingCount": len(missing),
        "extraCount": len(extra),
        "missing": [endpoint.__dict__ for endpoint in missing],
        "extra": [endpoint.__dict__ for endpoint in extra],
        "interfaces": {
            name: [endpoint.__dict__ for endpoint in endpoints]
            for name, endpoints in sorted(by_interface.items())
        },
    }

    json_path = out_dir / "openapi-coverage.json"
    json_path.write_text(json.dumps(report_json, indent=2) + "\n", encoding="utf-8")

    md_lines = [
        "# OpenAPI Coverage Report",
        "",
        f"- OpenAPI URL: `{openapi_url}`",
        f"- Expected endpoints: `{len(expected)}`",
        f"- Documented endpoints: `{len(actual)}`",
        f"- Matched endpoints: `{len(matched)}`",
        f"- Missing endpoints: `{len(missing)}`",
        f"- Extra endpoints: `{len(extra)}`",
        "",
    ]

    if missing:
        md_lines.extend(["## Missing in OpenAPI", ""])
        md_lines.extend([f"- `{endpoint.method} {endpoint.path}`" for endpoint in missing])
        md_lines.append("")

    if extra:
        md_lines.extend(["## Extra in OpenAPI", ""])
        md_lines.extend([f"- `{endpoint.method} {endpoint.path}`" for endpoint in extra])
        md_lines.append("")

    if not missing and not extra:
        md_lines.extend(["## Result", "", "Coverage is complete: expected and documented endpoints match.", ""])

    md_lines.extend(["## Contract Endpoints by Interface", ""])
    for interface_name, endpoints in sorted(by_interface.items()):
        md_lines.append(f"### `{interface_name}`")
        if endpoints:
            md_lines.extend([f"- `{endpoint.method} {endpoint.path}`" for endpoint in endpoints])
        else:
            md_lines.append("- No request mappings found")
        md_lines.append("")

    md_path = out_dir / "openapi-coverage.md"
    md_path.write_text("\n".join(md_lines), encoding="utf-8")

    print(f"Wrote {json_path}")
    print(f"Wrote {md_path}")

    if missing or extra:
        print("Coverage check failed.")
        return 1

    print("Coverage check passed.")
    return 0


def main() -> int:
    args = parse_args()

    expected, by_interface = load_expected_endpoints(Path(args.api_sources))
    actual = load_actual_endpoints(args.openapi_url)

    return write_report(
        out_dir=Path(args.out_dir),
        openapi_url=args.openapi_url,
        expected=expected,
        actual=actual,
        by_interface=by_interface,
    )


if __name__ == "__main__":
    sys.exit(main())

