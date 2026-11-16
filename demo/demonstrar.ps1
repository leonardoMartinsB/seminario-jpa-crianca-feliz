param([string]$Base = 'http://127.0.0.1:8081/api')

$ErrorActionPreference = 'Stop'

function Request {
    param([string]$Method, [string]$Route, [int]$Expected, [hashtable]$Data)
    $request = @{ Uri = "$Base$Route"; Method = $Method; SkipHttpErrorCheck = $true }
    if ($null -ne $Data) {
        $request.ContentType = 'application/json; charset=utf-8'
        $request.Body = $Data | ConvertTo-Json -Compress
    }
    $response = Invoke-WebRequest @request
    if ($response.StatusCode -ne $Expected) {
        throw "$Method $Route retornou $($response.StatusCode), esperado $Expected. $($response.Content)"
    }
    if ($Expected -eq 201 -and !$response.Headers.Location) {
        throw "Resposta de criação sem Location em $Route"
    }
    Write-Host "$Expected $Method $Route"
    $conteudo = $response.Content
    if ($conteudo -is [byte[]]) { $conteudo = [Text.Encoding]::UTF8.GetString($conteudo) }
    if ($conteudo) { return ConvertFrom-Json -InputObject $conteudo -NoEnumerate }
}

$null = Request GET '/responsaveis' 200
$null = Request GET '/atendidos' 200
$null = Request GET '/fichas-socioeconomicas' 200
$nomes = @{nome='Responsavel Roteiro'; telefone=''}
$r = Request POST '/responsaveis' 201 $nomes
$null = Request GET "/responsaveis/$($r.id)" 200
$nomes.nome = 'Responsavel Atualizado'
$null = Request PUT "/responsaveis/$($r.id)" 200 $nomes

$cadastro = @{nome='Assistido Roteiro'; dataNascimento='2017-05-20'; responsavelId=$r.id}
$a = Request POST '/atendidos' 201 $cadastro
if ($a.responsavel.id -ne $r.id -or $a.responsavel.nome -ne $nomes.nome) { throw 'Vínculo incorreto' }
$null = Request GET "/atendidos/$($a.id)" 200
$cadastro.nome = 'Assistido Atualizado'
$null = Request PUT "/atendidos/$($a.id)" 200 $cadastro
$lista = Request GET "/responsaveis/$($r.id)/atendidos" 200
if ($lista.Count -ne 1 -or $lista[0].id -ne $a.id) { throw 'Consulta inversa incorreta' }

$ficha = @{atendidoId=$a.id; rendaFamiliar=1200; pessoasNoDomicilio=3}
$f = Request POST '/fichas-socioeconomicas' 201 $ficha
if ($f.rendaPorPessoa -ne 400) { throw 'Cálculo de renda incorreto' }
$null = Request GET "/fichas-socioeconomicas/$($f.id)" 200
$relacao = Request GET "/atendidos/$($a.id)/ficha-socioeconomica" 200
if ($relacao.id -ne $f.id) { throw 'Ficha incorreta para o atendido' }
$ficha.rendaFamiliar = 1250
$editada = Request PUT "/fichas-socioeconomicas/$($f.id)" 200 $ficha
if ($editada.rendaPorPessoa -ne 416.67) { throw 'Arredondamento incorreto' }
$null = Request POST '/fichas-socioeconomicas' 409 $ficha
$ficha.pessoasNoDomicilio = 0
$erro = Request POST '/fichas-socioeconomicas' 400 $ficha
if (!$erro.campos.pessoasNoDomicilio) { throw 'Campo inválido ausente da resposta' }
$null = Request POST '/atendidos' 404 @{nome='Atendido Demo'; dataNascimento='2017-05-20'; responsavelId=999999}
$null = Request DELETE "/responsaveis/$($r.id)" 409
$null = Request DELETE "/atendidos/$($a.id)" 409
$null = Request DELETE "/fichas-socioeconomicas/$($f.id)" 204
$null = Request DELETE "/atendidos/$($a.id)" 204
$null = Request DELETE "/responsaveis/$($r.id)" 204
$null = Request GET "/atendidos/$($a.id)" 404
Write-Host 'Demonstração concluída.'
