import os
import re

base_dir = r"C:\Users\KEVIT\git\local-csms\localcsms"
out_path = r"C:\Users\KEVIT\.gemini\antigravity\brain\c145687d-b6e6-4bb0-b656-14c77a677b3f\model_terms_glossary.md"

terms_dict = {}

# 1. Parse all java files in `domain` and `entity` folders
for root, dirs, files in os.walk(base_dir):
    if "domain" in root or "entity" in root:
        for f in files:
            if f.endswith(".java"):
                java_path = os.path.join(root, f)
                try:
                    with open(java_path, 'r', encoding='utf-8', errors='ignore') as jf:
                        content = jf.read()
                    
                    lines = content.split('\n')
                    komment = []
                    for line in lines:
                        line = line.strip()
                        if line.startswith('//') or line.startswith('*') or line.startswith('/**'):
                            komment.append(line)
                        elif line.startswith('private '):
                            parts = line.split()
                            if len(parts) >= 3:
                                prop_name = parts[2].rstrip(';')
                                if '=' in prop_name:
                                    prop_name = prop_name.split('=')[0]
                                k_desc = " ".join(komment).replace('*', '').replace('/', '').strip()
                                k_desc = re.sub(r'\s+', ' ', k_desc)
                                
                                if prop_name not in terms_dict or len(k_desc) > len(terms_dict[prop_name].get('desc', '')):
                                    terms_dict[prop_name] = {
                                        "desc": k_desc,
                                        "module": f.replace('.java', '')
                                    }
                            komment = []
                        elif not line:
                            continue
                        else:
                            komment = []
                except:
                    pass

# 2. Parse XML mapper files to get column mapping
result_maps = {}
for root, dirs, files in os.walk(base_dir):
    for f in files:
        if f.endswith("sql.xml"):
            xml_path = os.path.join(root, f)
            try:
                module = f.replace('_sql.xml', '')
                with open(xml_path, 'r', encoding='utf-8', errors='ignore') as xf:
                    content = xf.read()
                    matches = re.findall(r'<result\s+column="([^"]+)"\s+property="([^"]+)"', content)
                    for col, prop in matches:
                        if prop not in result_maps:
                            result_maps[prop] = set()
                        result_maps[prop].add((col, module))
            except:
                pass

# 3. Combine and generate markdown
output_table = [
    "# Domain Models and SQL Mapper Glossary\n",
    "This glossary extracts the mapping between domain variables, SQL columns, and their Korean descriptions from Java domain classes and `*sql.xml` mappers.\n",
    "| Domain Object / Module | Variable Name (Property) | Database Column | Logical Name (Description) |",
    "|---|---|---|---|"
]

# We will index by property to deduplicate
processed_props = set()

for prop, mappings in result_maps.items():
    for col, module in mappings:
        info = terms_dict.get(prop, {"desc": "", "module": module})
        desc = info['desc']
        # Remove raw schema definition like "VARCHAR(20) NOT NULL"
        desc = re.sub(r'(?i)(varchar|char|int|tinyint|datetime|number|numeric)\s*\(\d+(,\d+)?\)?\s*(not null)?', '', desc)
        # Clean col names from desc to avoid repetition
        desc = desc.replace(col, '').replace(',', '').strip()
        
        output_table.append(f"| {module} | `{prop}` | `{col}` | {desc} |")
        processed_props.add(prop)

# Also add properties found in models but not in xml, just in case
for prop, info in terms_dict.items():
    if prop not in processed_props:
        desc = info['desc']
        if desc:  # Only add if it has a comment
            desc = re.sub(r'(?i)(varchar|char|int|tinyint|datetime|number|numeric)\s*\(\d+(,\d+)?\)?\s*(not null)?', '', desc)
            output_table.append(f"| {info['module']} | `{prop}` |  | {desc} |")

with open(out_path, 'w', encoding='utf-8') as f:
    f.write("\n".join(output_table))

print("Glossary generated at:", out_path)
