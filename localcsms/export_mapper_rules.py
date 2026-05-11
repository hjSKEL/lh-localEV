import os
import re

base_dir = r"C:\Users\KEVIT\git\local-csms\localcsms"
out_path = r"C:\Users\KEVIT\git\local-csms\artifact\mapper_naming_conventions.md"

method_prefixes = {}
total_methods = 0

# Extract methods from Mapper interfaces
for root, dirs, files in os.walk(base_dir):
    if "entity" in root:
        for f in files:
            if f.endswith("Mapper.java"):
                java_path = os.path.join(root, f)
                try:
                    with open(java_path, 'r', encoding='utf-8', errors='ignore') as jf:
                        content = jf.read()
                        
                        # Very basic regex to find method declarations in interfaces
                        # Ex: Integer countUserBySearchCond(UserSearchCond searchCond);
                        # Ex: List<User> selectUserBySearchCond(UserSearchCond searchCond);
                        # Matches: <ReturnType> <methodName>(<Args>);
                        method_pattern = re.compile(r'^\s*[\w<>,\s\[\]]+\s+(\w+)\s*\(.*?\)\s*;', re.MULTILINE)
                        methods = method_pattern.findall(content)
                        
                        for m_name in methods:
                            total_methods += 1
                            # extract the prefix (verb)
                            # Assuming camelCase usually starts with a verb: select, insert, update, delete, count, etc.
                            # We can just check the prefix based on uppercase split
                            match = re.match(r'^([a-z]+)', m_name)
                            if match:
                                prefix = match.group(1)
                                if prefix not in method_prefixes:
                                    method_prefixes[prefix] = []
                                method_prefixes[prefix].append((m_name, f))
                except Exception as e:
                    print(f"Error parsing {f}: {e}")

# Format output
output = [
    "# MyBatis Mapper Naming Conventions\n",
    f"Based on `{total_methods}` methods found in `27` `*Mapper.java` files across `*-entity` components, here are the naming conventions:\n",
    "## 1. Prefix (Verb) Analysis\n",
    "| Prefix | Count | Example Formats | Summary |",
    "|---|---|---|---|"
]

# We want to show a few examples for each prefix and deduce the common suffix pattern
for prefix, occurrences in sorted(method_prefixes.items(), key=lambda x: len(x[1]), reverse=True):
    count = len(occurrences)
    
    # Analyze common patterns within this prefix
    # e.g., selectXxx, selectXxxByYyy, selectXxxList
    patterns = set()
    for m_name, mapper_file in occurrences:
        entity_name = mapper_file.replace('Mapper.java', '')
        # Replace entity name in the method name to see generic pattern
        pattern = m_name.replace(entity_name, '{Entity}')
        # Simplify pattern
        patterns.add(pattern)
    
    # Pick top 3 most common/representative patterns
    examples_list = list(patterns)
    # Just show a few generic examples to avoid too much clutter
    examples = "<br>".join(examples_list[:4])
    if len(examples_list) > 4:
         examples += "<br>..."
         
    # Generate generic description
    desc = ""
    if prefix == "select":
        desc = "상세 조회, 조건 조회, 리스트 조회 등 데이터를 가져올 때 사용"
    elif prefix == "insert":
        desc = "신규 데이터 등록 시 사용"
    elif prefix == "update":
        desc = "기존 데이터 수정 시 사용"
    elif prefix == "delete":
        desc = "데이터 삭제 시 사용"
    elif prefix == "count":
        desc = "조건에 맞는 데이터의 총 개수(Total Count)를 가져올 때 주로 페이징 처리와 함께 사용"
    else:
        desc = "기타 특수 목적 함수"
        
    output.append(f"| `{prefix}` | {count} | {examples} | {desc} |")

output.append("\n## 2. Standard Naming Rules (Derived)\n")
output.append("- **등록 (Create)**: `insert{Entity}` 형식 사용 (예: `insertUser`)")
output.append("- **조회 (Read)**: `select`로 시작. ")
output.append("  - 단건 조회: PK나 유니크 키 조회 시 `select{Entity}ById` 또는 `select{Entity}By{Condition}`")
output.append("  - 다건 조회 (Search): 검색 조건이 있을 경우 `select{Entity}BySearchCond`")
output.append("  - 전체/리스트 조회: `select{Entity}List`")
output.append("- **수정 (Update)**: `update{Entity}` 형식 사용 (예: `updateUser`)")
output.append("- **삭제 (Delete)**: `delete{Entity}` 형식 사용 (단건은 PK 기준, 다건은 조건 기준)")
output.append("- **건수 (Count)**: 검색 조건에 대한 데이터 건수를 구할 때 `count{Entity}BySearchCond` 패턴 주로 사용")

with open(out_path, 'w', encoding='utf-8') as f:
    f.write("\n".join(output))

print(f"Naming conventions documented at: {out_path}")
