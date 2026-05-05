$ErrorActionPreference = "Stop"

$connectorConfig = Get-Content -Raw -Path "$PSScriptRoot\debezium-outbox-connector.json"

Invoke-RestMethod `
  -Method Put `
  -Uri "http://localhost:8083/connectors/usercheck-outbox-connector/config" `
  -ContentType "application/json" `
  -Body (($connectorConfig | ConvertFrom-Json).config | ConvertTo-Json -Depth 20)

