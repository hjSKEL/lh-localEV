import os
import re

base_dir = r"C:\Users\KEVIT\git\local-csms\localcsms"
out_path = r"C:\Users\KEVIT\git\local-csms\artifact\service_extprocess_naming_conventions.md"

def extract_methods(file_suffix, dir_keyword):
    method_prefixes = {}
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
                            
                            # Regex to find method declarations in interfaces
                            method_pattern = re.compile(r'^\s*[\w<>,\s\[\]]+\s+(\w+)\s*\(.*?\)\s*;', re.MULTILINE)
                            methods = method_pattern.findall(content)
                            
                            for m_name in methods:
                                total_methods += 1
                                match = re.match(r'^([a-z]+)', m_name)
                                if match:
                                    prefix = match.group(1)
                                    if prefix not in method_prefixes:
                                        method_prefixes[prefix] = []
                                    method_prefixes[prefix].append((m_name, f))
                    except Exception as e:
                        pass
    return method_prefixes, total_methods, file_count

svc_prefixes, svc_total, svc_files = extract_methods("Service.java", "process")
ext_prefixes, ext_total, ext_files = extract_methods("ExtProcess.java", "external")

# Format output
output = [
    "# Service & ExtProcess Naming Conventions\n",
    "This document outlines the method naming conventions used in `*Service.java` (Business Logic) and `*ExtProcess.java` (External Interface) files.\n",
]

def format_section(title, component_name, prefixes, total, f_count):
    lines = [
        f"## {title} (`{component_name}` 컴포넌트)",
        f"**총 파일 수:** {f_count} 개 | **총 메서드 수:** {total} 개\n",
        "| Prefix (Verb) | Count | Example Formats | Summary |",
        "|---|---|---|---|"
    ]
    
    for prefix, occurrences in sorted(prefixes.items(), key=lambda x: len(x[1]), reverse=True):
        count = len(occurrences)
        patterns = set()
        for m_name, file_name in occurrences:
            base_name = file_name.replace('Service.java', '').replace('ExtProcess.java', '')
            pattern = m_name.replace(base_name, '{Entity}')
            patterns.add(pattern)
            
        examples_list = list(patterns)
        examples = "<br>".join(examples_list[:4])
        if len(examples_list) > 4:
             examples += "<br>..."
             
        # Generic descriptions
        desc = ""
        if prefix in ["get", "find", "search", "select"]:
            desc = "데이터 조회 로직 (상세, 리스트, 조건 검색 등)"
        elif prefix in ["register", "add", "insert", "create"]:
            desc = "신규 데이터 등록/생성 비즈니스 로직"
        elif prefix in ["modify", "update", "edit"]:
            desc = "기존 데이터 수정 로직"
        elif prefix in ["remove", "delete"]:
            desc = "데이터 삭제 로직"
        elif prefix in ["count"]:
            desc = "데이터 건수 조회 로직 (주로 페이징)"
        elif prefix in ["process", "execute", "handle", "check"]:
            desc = "특정 비즈니스 워크플로우 처리"
        else:
            desc = "기타 비즈니스/연동 로직"
            
        lines.append(f"| `{prefix}` | {count} | {examples} | {desc} |")
    return "\n".join(lines) + "\n\n"

output.append(format_section("1. Service 인터페이스 명명 규칙", "*-process", svc_prefixes, svc_total, svc_files))

output.append(format_section("2. ExtProcess 인터페이스 명명 규칙", "*-external", ext_prefixes, ext_total, ext_files))

output.append("## 3. Standard Naming Rules (Derived)\n")
output.append("### Service (`*-process`) 규칙")
output.append("- **등록 (Create)**: 주로 `register...`, `insert...`, `add...` 사용")
output.append("- **조회 (Read)**: 주로 `get...`, `find...`, `search...` (Mapper와 달리 비즈니스 관점의 용어 사용)")
output.append("- **수정 (Update)**: 주로 `modify...`, `update...` 사용")
output.append("- **삭제 (Delete)**: 주로 `remove...`, `delete...` 사용")
output.append("- **기타 비즈니스**: `process...`, `execute...` 등 행위 중심의 명명\n")

output.append("### ExtProcess (`*-external`) 규칙")
output.append("- 외부 시스템 또는 다른 MSA 도메인과의 통신을 담당하므로, 내부 Service와 유사하거나 외부 API 엔드포인트에 매핑되는 명사형/동사형 조합을 가짐.")

with open(out_path, 'w', encoding='utf-8') as f:
    f.write("\n".join(output))

print(f"Naming conventions documented at: {out_path}")
