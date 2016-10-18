#!/usr/bin/env bash
#
# Baixa os dados que usamos de convênios.
# Embora o arquivo no servidor tenha sempre o mesmo nome, ele é atualizado de
# tempos em tempos. Ainda não sabemos a periodicidade.
#
set -e
set -u

adddate() {
    while IFS= read -r line; do
        echo "[$(date)] $line"
    done
}

curl -z $1/convenios.zip -o $1/convenios.zip  'http://portal.convenios.gov.br/images/docs/CGSIS/csv/siconv_convenio.csv.zip' -H 'Accept-Encoding: gzip, deflate, sdch' -H 'Accept-Language: en-US,en;q=0.8,pt;q=0.6' -H 'Upgrade-Insecure-Requests: 1' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8'  -H 'Connection: keep-alive' --compressed --insecure
curl -z $1/propostas.zip -o $1/propostas.zip  'http://portal.convenios.gov.br/images/docs/CGSIS/csv/siconv_proposta.csv.zip' -H 'Accept-Encoding: gzip, deflate, sdch' -H 'Accept-Language: en-US,en;q=0.8,pt;q=0.6' -H 'Upgrade-Insecure-Requests: 1' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' -H 'Connection: keep-alive' --compressed --insecure
curl -z $1/historico.zip -o $1/historico.zip  'http://portal.convenios.gov.br/images/docs/CGSIS/csv/siconv_historico_situacao.csv.zip' -H 'Accept-Encoding: gzip, deflate, sdch' -H 'Accept-Language: en-US,en;q=0.8,pt;q=0.6' -H 'Upgrade-Insecure-Requests: 1' -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' -H 'Connection: keep-alive' --compressed --insecure
cd $1
unzip -o convenios.zip | adddate # resultado: siconv_convenio.csv
unzip -o propostas.zip | adddate # siconv_proposta.csv
unzip -o historico.zip | adddate # siconv_historico_situacao.csv
cd -
