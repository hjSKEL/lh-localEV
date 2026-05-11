import os
import xml.etree.ElementTree as ET

base_dir = r"C:\Users\KEVIT\git\local-csms\localcsms"
out_path = r"C:\Users\KEVIT\git\local-csms\artifact\sql_mapper_naming_conventions.md"

query_prefixes = {
    'select': [],
    'insert': [],
    'update': [],
    'delete': []
}
total_queries = 0
file_count = 0

for root, dirs, files in os.walk(base_dir):
    if "entity" in root and "resources" in root: # only looking at source xml, not target/classes
        for f in files:
            if f.endswith("sql.xml"):
                file_count += 1
                xml_path = os.path.join(root, f)
                try:
                    # Very basic parsing, stripping namespace if needed but usually simple in mybatis
                    tree = ET.parse(xml_path)
                    root_elem = tree.getroot()
                    
                    for tag in ['select', 'insert', 'update', 'delete']:
                        for elem in root_elem.findall(tag):
                            q_id = elem.get('id')
                            if q_id:
                                total_queries += 1
                                query_prefixes[tag].append((q_id, f))
                except Exception as e:
                    pass

output = [
    "# SQL Mapper (XML) Naming Conventions\n",
    "This document outlines the Query ID naming conventions used in `*sql.xml` files within the `*-entity` components.\n",
    f"**총 파일 수:** {file_count} 개 | **총 쿼리 수:** {total_queries} 개\n",
    "## 1. Query ID Prefix (Verb) Analysis",
    "| Query Type | Count | Example Formats | Summary |",
    "|---|---|---|---|"
]

for tag, occurrences in query_prefixes.items():
    count = len(occurrences)
    
    patterns = set()
    for q_id, f_name in occurrences:
        base_name = f_name.replace('_sql.xml', '')
        # Simple camel case replace for pattern
        # Try to find base_name in q_id ignoring case
        import re
        pattern = re.sub(base_name, '{Entity}', q_id, flags=re.IGNORECASE)
        patterns.add(pattern)
        
    examples_list = list(patterns)
    # limit to 5 examples
    examples = "<br>".join(examples_list[:5])
    if len(examples_list) > 5:
        examples += "<br>..."
        
    desc = ""
    if tag == "select":
        desc = "데이터 조회용 쿼리 (단건, 다건, 페이징, 카운트 포함)"
    elif tag == "insert":
        desc = "새로운 데이터 삽입 쿼리"
    elif tag == "update":
        desc = "기존 데이터 변경 쿼리"
    elif tag == "delete":
        desc = "데이터 삭제 쿼리"
        
    output.append(f"| `<{tag}>` | {count} | {examples} | {desc} |")


output.append("\n## 2. Standard Naming Rules (Derived)\n")
output.append("- **등록 (Insert)**: `<insert id=\"insert{Entity}\">` 형태를 사용. 복수 등록 시 `insert{Entity}List` 등 사용.")
output.append("- **조회 (Select)**: `<select id=\"select{Entity}...\">` 형태를 사용.")
output.append("  - 단건 조회: `select{Entity}ById` 등 식별자 명시")
output.append("  - 다건/조건 조회: `select{Entity}BySearchCond` 등 SearchCond 접미사 사용")
output.append("  - 개수 조회: `count{Entity}BySearchCond` (페이징 시 select와 쌍을 이룸)")
output.append("- **수정 (Update)**: `<update id=\"update{Entity}\">` 형태를 사용.")
output.append("- **삭제 (Delete)**: `<delete id=\"delete{Entity}\">` 형태를 사용.")
output.append("\n> 참고: XML Mapper의 `id` 값은 `*Mapper.java` (인터페이스)의 메서드명과 1:1로 정확히 일치해야 합니다.")

with open(out_path, 'w', encoding='utf-8') as f:
    f.write("\n".join(output))

# Let's also update the all_naming_conventions.md to include this new section
all_conv_path = os.path.join(r"C:\Users\KEVIT\git\local-csms\artifact", "all_naming_conventions.md")
if os.path.exists(all_conv_path):
    with open(all_conv_path, 'a', encoding='utf-8') as f:
        f.write("\n---\n")
        f.write("\n".join(output))
        f.write("\n")

print(f"SQL Mapper naming conventions documented at: {out_path} and appended to all_naming_conventions.md")
