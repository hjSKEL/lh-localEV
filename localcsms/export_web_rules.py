import os
import re

base_dir = r"C:\Users\KEVIT\git\local-csms\localcsms\web\admin-web"
out_path = r"C:\Users\KEVIT\git\local-csms\artifact\controller_resource_naming_conventions.md"

def extract_methods_and_endpoints(file_suffix, dir_keyword):
    method_prefixes = {}
    endpoint_patterns = []
    total_methods = 0
    file_count = 0
    
    for root, dirs, files in os.walk(base_dir):
        if dir_keyword in root:
            for f in files:
                if f.endswith(file_suffix):
                    file_count += 1
                    java_path = os.path.join(root, f)
                    try:
                        with open(java_path, 'r', encoding='utf-8', errors='ignore') as jf:
                            content = jf.read()
                            
                            # Find method declarations (public)
                            method_pattern = re.compile(r'^\s*public\s+[\w<>\?,\s\[\]]+\s+(\w+)\s*\(.*?\)\s*(?:throws\s+[\w,\s]+)?\s*\{', re.MULTILINE)
                            methods = method_pattern.findall(content)
                            
                            for m_name in methods:
                                total_methods += 1
                                match = re.match(r'^([a-z]+)', m_name)
                                if match:
                                    prefix = match.group(1)
                                    if prefix not in method_prefixes:
                                        method_prefixes[prefix] = []
                                    method_prefixes[prefix].append((m_name, f))
                                    
                            # Extract mapping annotations
                            mapping_pattern = re.compile(r'@(?:Get|Post|Put|Delete|Request)Mapping\s*\(\s*(?:value\s*=\s*)?(?:\{)?\s*["\']([^"\']+)["\']')
                            mappings = mapping_pattern.findall(content)
                            for m in mappings:
                                endpoint_patterns.append((m, f))
                                
                    except Exception as e:
                        pass
    return method_prefixes, endpoint_patterns, total_methods, file_count

ctrl_prefixes, ctrl_endpoints, ctrl_total, ctrl_files = extract_methods_and_endpoints("Controller.java", "controller")
resc_prefixes, resc_endpoints, resc_total, resc_files = extract_methods_and_endpoints("Resource.java", "resource")

output = [
    "# Web Layer Naming Conventions (`admin-web` Component)\n",
    "This document outlines the method naming and endpoint routing conventions used in `*Controller.java` (Page/View routing) and `*Resource.java` (REST API data endpoints) files within the `admin-web` component.\n",
]

def format_section(title, component_type, prefixes, endpoints, total, f_count):
    lines = [
        f"## {title} (`{component_type}`)",
        f"**총 파일 수:** {f_count} 개 | **총 메서드 수:** {total} 개\n",
        "### 1. Method Prefix (Verb) Analysis",
        "| Prefix (Verb) | Count | Example Formats | Summary |",
        "|---|---|---|---|"
    ]
    
    for prefix, occurrences in sorted(prefixes.items(), key=lambda x: len(x[1]), reverse=True):
        count = len(occurrences)
        patterns = set()
        for m_name, file_name in occurrences:
            base_name = file_name.replace('Controller.java', '').replace('Resource.java', '')
            pattern = m_name.replace(base_name, '{Entity}')
            patterns.add(pattern)
            
        examples_list = list(patterns)
        examples = "<br>".join(examples_list[:4])
        if len(examples_list) > 4:
             examples += "<br>..."
             
        desc = ""
        if prefix in ["show", "view", "go", "move", "index", "load"]:
            desc = "HTML/JSP 뷰 페이지로 이동 (Controller 주로 사용)"
        elif prefix in ["get", "find", "search", "list", "read", "retrieve"]:
            desc = "데이터 조회용 (API 엔드포인트)"
        elif prefix in ["add", "insert", "create", "register"]:
            desc = "신규 데이터 생성/등록 API"
        elif prefix in ["modify", "update", "edit"]:
            desc = "데이터 수정 API"
        elif prefix in ["remove", "delete"]:
            desc = "데이터 삭제 API"
        elif prefix in ["download", "upload", "excel"]:
            desc = "파일 입출력 로직 (엑셀 다운로드 등)"
        else:
            desc = "기타 비즈니스 처리 및 특수 로직 (로그인, 검증 등)"
            
        lines.append(f"| `{prefix}` | {count} | {examples} | {desc} |")
        
    lines.append("\n### 2. URL Endpoint Mapping Rules")
    
    top_endpoints = [e for e, f in endpoints]
    common_start_patterns = set()
    for e in top_endpoints:
        if e.startswith('/'):
            parts = e.split('/')
            if len(parts) > 1 and parts[1]:
                common_start_patterns.add('/' + parts[1])
                
    if common_start_patterns:
        lines.append(f"- **주요 URL Prefix**: `{', '.join(list(common_start_patterns)[:8])}`")
    
    sample_endpoints = list(set([e for e, f in endpoints]))[:10]
    lines.append("- **URL 패턴 예시**: ")
    for e in sample_endpoints:
         lines.append(f"  - `{e}`")
         
    return "\n".join(lines) + "\n\n"

output.append(format_section("1. Controller 명명 규칙 (화면 이동 및 View 반환)", "controller", ctrl_prefixes, ctrl_endpoints, ctrl_total, ctrl_files))
output.append(format_section("2. Resource 명명 규칙 (REST API 데이터 제공)", "resource", resc_prefixes, resc_endpoints, resc_total, resc_files))

output.append("## 3. Standard Naming Rules (Derived)\n")
output.append("### 화면 라우팅 (Controller)")
output.append("- **역할**: JSP/HTML과 같은 뷰(View) 페이지 반환 및 엑셀 다운로드")
output.append("- **URL 규칙**: 주로 `/system`, `/organization`, `/charger` 등 도메인/메뉴 위계에 맞는 경로를 가짐.")
output.append("- **메서드 접두어**: `show...`, `index`, `excel...` 등 뷰 렌더링 및 파일 다운로드를 나타내는 동사 활용.\n")

output.append("### API 엔드포인트 (Resource)")
output.append("- **역할**: JSON 데이터를 반환하는 RESTful API (프론트엔드 AJAX 요청 처리)")
output.append("- **URL 규칙**: 주로 `/api/...` 형태의 prefix를 가지며, RESTful 특징을 살려 명사 위주의 자원(Resource)을 명시")
output.append("- **메서드 접두어**: `get...`, `remove...`, `modify...`, `register...`, `retrieve...` 등 HTTP 메서드 성격(CRUD)에 매핑되는 동사 활용.")

with open(out_path, 'w', encoding='utf-8') as f:
    f.write("\n".join(output))

print(f"Web layer naming conventions documented at: {out_path}")
