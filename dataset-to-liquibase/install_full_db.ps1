$StartTime = $(get-date)
# Install dependency lxml
Write-Host "Installing lxml python dependency"
python -m venv .venv
.venv\Scripts\activate
python -m pip install --upgrade pip
python -m pip install lxml
$elapsedTime = $(get-date) - $StartTime
Write-host "Done in $($elapsedTime.ToString("hh\:mm\:ss"))"  
# Run dataset_to_liquibase.py
$StartTimeDatasetToLiquibase = $(get-date)
Write-Host "Running dataset_to_liquibase.py..."
python dataset_to_liquibase.py
$elapsedTime = $(get-date) - $StartTimeDatasetToLiquibase
Write-host "Wrote all changeset in $($elapsedTime.ToString("hh\:mm\:ss"))"  

$StartTimeCleanXml = $(get-date)
# Run clean_xml.py
Write-Host "Running clean_xml.py..."
python clean_xml.py
$elapsedTime = $(get-date) - $StartTimeCleanXml
Write-host "Cleaned all changeset in $($elapsedTime.ToString("hh\:mm\:ss"))"  


# Run create_changeset.py
Write-Host "Running create_changeset.py..."
python create_changeset.py

# Copy all files from liquibase_chunks to target folder
$sourceFolder = "liquibase_chunks"
$targetFolder = "..\cinematch-backend-service\cinematch-backend\src\main\resources\db\changelog"  # Change this to your desired destination

# Ensure the target folder exists
if (!(Test-Path $targetFolder)) {
    New-Item -ItemType Directory -Path $targetFolder | Out-Null
}


Write-Host "Create $targetFolder\dataset_chunks folder..."    
New-Item -ItemType Directory -Force -Path "$targetFolder\dataset_chunks..."

Write-Host "Copying files from $sourceFolder to $targetFolder\dataset_chunks..."    
Copy-Item "$sourceFolder\*" -Destination "$targetFolder\dataset_chunks" -Recurse -Force

Write-Host "Copying changeset to $targetFolder..."
Copy-Item "changeset.xml" -Destination "$targetFolder\full-dataset-insert.xml" -Force

