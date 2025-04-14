import os

def clean_text_file(input_path, output_path=None):
    with open(input_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # Perform the replacements
    content = content.replace('&quot;&quot;', "'")
    content = content.replace('&quot;', "'")
    content = content.replace('&amp;', 'and')

    # Decide where to write the output
    output_path = output_path or input_path
    with open(output_path, 'w', encoding='utf-8') as file:
        file.write(content)

    print(f"Cleaned text saved to: {output_path}")

def clean_folder(folder_path):
    for root, _, files in os.walk(folder_path):
        for filename in files:
            if filename.lower().endswith('.xml'):
                full_path = os.path.join(root, filename)
                clean_text_file(full_path)
# Example usage
clean_folder('liquibase_chunks')  # Replace with your folder path