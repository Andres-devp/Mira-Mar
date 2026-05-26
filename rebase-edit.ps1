param([string]$todoPath)

$lines = Get-Content -Path $todoPath
$lines = $lines -replace '^pick\s+959e965\b', 'edit 959e965'
Set-Content -Path $todoPath -Value $lines
