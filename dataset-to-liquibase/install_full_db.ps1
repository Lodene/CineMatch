# Install dependency lxml
Write-Host "Installing lxml python dependency"
python -m venv .venv
.venv\Scripts\activate
python -m pip install --upgrade pip
python -m pip install lxml
# Run dataset_to_liquibase.py
Write-Host "Running dataset_to_liquibase.py..."
python dataset_to_liquibase.py

# Run clean_xml.py
Write-Host "Running clean_xml.py..."
python clean_xml.py

# Run create_changeset.py
Write-Host "Running create_changeset.py..."
python create_changeset.py

# Copy all files from liquibase_chunks to target folder
$sourceFolder = "liquibase_chunks"
$targetFolder = "../cinematch-backend-service\cinematch-backend\src\main\resources\db\changelog"  # Change this to your desired destination

# Ensure the target folder exists
if (!(Test-Path $targetFolder)) {
    New-Item -ItemType Directory -Path $targetFolder | Out-Null
}

Write-Host "Copying files from $sourceFolder to $targetFolder..."
Copy-Item "$sourceFolder\*" -Destination $targetFolder -Recurse -Force

Write-Host "Copying changeset to $targetFolder..."
Copy-Item "changeset.xml" -Destination "$targetFolder\full-db-insert.xml" -Force

