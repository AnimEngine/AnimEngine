$Url = "http://127.0.0.1:5001/animengine-fb858/us-central1/register"
$Body = @{
    Email = "a@gmail.com"
    Password = "123456"
    Type = "fan"

    fName = "Dani"
}

$Wrapper = @{
    data = $Body
}
Invoke-RestMethod -Method 'Post' -Uri $Url -Body ($Wrapper|ConvertTo-Json) -ContentType "application/json"
