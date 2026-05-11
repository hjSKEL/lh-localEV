import os

base_dir = r"C:\Users\KEVIT\git\local-csms\artifact"
out_path = os.path.join(base_dir, "all_naming_conventions.md")

files_to_merge = [
    "mapper_naming_conventions.md",
    "service_extprocess_naming_conventions.md",
    "controller_resource_naming_conventions.md"
]

merged_content = ["# 통합 명명 규칙 가이드 (Naming Conventions Guide)\n"]
merged_content.append("본 문서는 `local-csms` 프로젝트의 주요 계층별(Mapper, Service, Web Layer) 메서드 명명 규칙 및 URL 엔드포인트 라우팅 패턴을 통합하여 정리한 문서입니다.\n")

for f_name in files_to_merge:
    f_path = os.path.join(base_dir, f_name)
    if os.path.exists(f_path):
        with open(f_path, 'r', encoding='utf-8') as f:
            content = f.read()
            # Remove the top-level headers (#) and replace with (##) to fit the consolidated structure
            # To avoid messing up markdown, we just add a clear separator. 
            merged_content.append(f"\n---\n")
            merged_content.append(content)
            merged_content.append("\n")

with open(out_path, 'w', encoding='utf-8') as out_f:
    out_f.write("\n".join(merged_content))

print(f"Merged file created at: {out_path}")
