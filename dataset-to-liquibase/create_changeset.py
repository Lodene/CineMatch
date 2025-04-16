import os

def create_changeset(folder_path, changeset_path="changeset.xml"):
    includes = []

    for file in os.listdir(folder_path):
        full_path = os.path.join(folder_path, file)
        if os.path.isfile(full_path):
            includes.append(f'\t<include file="db/changelog/dataset_chunks/{file}"/>')

    with open(changeset_path, 'w', encoding='utf-8') as f:
        f.write('<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">')
        f.write('\n'.join(includes).strip())
        f.write('\n</databaseChangeLog>')
    print(f"Changeset created at: {changeset_path}")

# Example usage
create_changeset('liquibase_chunks', 'changeset.xml')
