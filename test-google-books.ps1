$baseUrl = "http://localhost:8081/api"
Write-Host "Testing Google Books integration..." -ForegroundColor Cyan

$r = Invoke-RestMethod -Uri "$baseUrl/books/search?q=harry+potter" -Method Get -Headers @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("library-admin:change-me")) }
Write-Host "Google results: $($r.Count)" -ForegroundColor Green

$r2 = Invoke-RestMethod -Uri "$baseUrl/books/search?q=zzzzzznotfound" -Method Get -Headers @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("library-admin:change-me")) }
Write-Host "Fallback results: $($r2.Count)" -ForegroundColor Yellow
