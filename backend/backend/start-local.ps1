param(
    [Parameter(Mandatory = $true)]
    [string]$DatabasePassword,

    [Parameter(Mandatory = $true)]
    [string]$JwtSecret,

    [Parameter(Mandatory = $true)]
    [string]$GroqApiKey
)

$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/smartstock"
$env:DATABASE_USERNAME = "postgres"
$env:DATABASE_PASSWORD = $DatabasePassword

$env:JWT_SECRET = $JwtSecret

$env:FRONTEND_URL = "http://localhost:4200"

$env:LLM_BASE_URL = "https://api.groq.com/openai/v1"
$env:LLM_API_KEY = $GroqApiKey
$env:LLM_MODEL = "openai/gpt-oss-20b"

$env:SHOW_SQL = "false"

mvn spring-boot:run
