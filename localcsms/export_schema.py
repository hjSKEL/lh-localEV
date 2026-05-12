import pymysql
import os

host = "121.170.152.235"
port = 16000
user = "lcsmsoper"
password = "3neiwQa0!"
db = "LOCAL_CSMS"
output_file = r"/Users/kevit/git.kevit/localcsms/localcsms/schema.sql"

conn = pymysql.connect(host=host, port=port, user=user, password=password, database=db, charset='utf8mb4')

try:
    with conn.cursor() as cursor:
        cursor.execute("SHOW TABLES")
        tables = [row[0] for row in cursor.fetchall()]
        
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(f"-- Schema Export for Database: {db}\n\n")
            for table in tables:
                cursor.execute(f"SHOW CREATE TABLE {table}")
                create_stmt = cursor.fetchone()[1]
                f.write(f"-- Table structure for {table}\n")
                f.write(f"DROP TABLE IF EXISTS `{table}`;\n")
                f.write(create_stmt + ";\n\n")
finally:
    conn.close()

print(f"Schema exported to {output_file}")
