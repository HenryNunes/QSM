# language: pt
# encoding: iso-8859-1
Funcionalidade: US001 - Incluir Instrumento Contratual com Poder Concedente
  Como um Administrador ou Analista SUPRIN
  Eu quero incluir um novo contrato com poder concedente no sistema,
  Para que seja possivel inserir as caracteristicas e iniciar o processo de controle deste contrato atraves do sistema.

  @Functional
  Esquema do Cenário: US001 - Validar acesso a funcionalidade de incluir Instrumento Contratual com Poder Concedente
    Dado que possuo o seguinte perfil <PERFIL>
    Quando acessar o sistema
    Então devo poder acessar a tela de incluir ninstrumento contratual com poder concedente

    Exemplos: 
      | PERFIL          |
      | Administrador   |
      | Analista SUPRIN |

  @Functional
  Esquema do Cenário: US001 - Não permitir acesso a funcionalidade incluir instrumento Contratual com poder concedente para perfil diferente de Administrador ou Analista Suprin
    Dado que possuo o seguinte perfil <PERFIL>
    Quando acessar o sistema
    Então não devo poder acessar a tela de incluir instrumento contratual com poder concedente

    Exemplos: 
      | PERFIL             |
      | Gestão de Demandas |

  @Functional
  Cenário: US001 - Acessar o formulario de Instrumento Contratual
    Dado que estou acessando o Sisplan
    E estou na tela de lista de contrato
    Quando clicar em Novo
    Entao formulario de Instrumento Contratual deve ser exibido
    E formulario deve conter os seguintes campos obrigatorios
      | campos obrigatórios |
      | Município           |
      | Tipo do Contrato    |
      | Nº do Contrato      |
      | Situação            |
    E os seguintes campos opcionais
      | campos opcionais               |
      | Versão                         |
      | Data da Assinatura             |
      | Prazo Contratual               |
      | Data do Vencimento do Contrato |
      | Convênio de Delegação          |
      | Objeto                         |
      | Área de Atuação                |
      | Data da Primeira Concessão     |
      | Observação do Contrato         |
      | Documento Pesquisável          |
      | Documento Imagem               |
    E os seguintes botoes: Salvar e Fechar

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual na situacao
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    Quando clicar em salvar
    Então Instrumento contratual deve ser salvo
    E deve ser gerado um número de contrato único
    E Instrumento contratual deve estar com a situação "Em contratação"
    E devo receber a mensagem: "Registro incluído com sucesso"

  @Integration
  Cenario: US001 - Geração do número de contrato
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    Quando clicar em salvar
    Então Instrumento contratual deve ser salvo
    E deve ser gerado um número de contrato único
    E o número de contrato deve ser número do último contrato criado no sistema (para o tipo de contrato informado na tela) mais 1

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual do tipo Contrato de Programa
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E Instrumento Contratual é do tipo Contrato de Programa
    Quando clicar em salvar
    Entao deve incluir o Instrumento Contratual
    E a versao do contrato sera igual a zero

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual do tipo Contrato de Concessao
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E Instrumento Contratual é do tipo Concessao
    Quando clicar em salvar
    Entao deve incluir o Instrumento Contratual
    E a versão do contrato sera igual nulo

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual do tipo Concessão
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E tipo de contrato é Concessão
    Quando clicar em salvar
    Então Instrumento Contratual deve ser salvo
    E deve ser gerado um número de contrato único
    E versão será nulo
    E Instrumento contratual deve estar no status

  @Functional
  Esquema do Cenario: US001 - Incluir um Instrumento Contratual com todos os campos opcionais preenchidos
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E o campo <NOME_DO_CAMPO> preenchido com o valor <VALOR_DO_CAMPO>
    Quando clicar em salvar
    Então Instrumento contratual deve ser salvo
    E deve ser gerado um número de contrato único
    E Instrumento contratual deve estar no status

    Exemplos: 
      | NOME_DO_CAMPO              | VALOR_DO_CAMPO        |
      | Data da Assinatura         | 08/04/2016            |
      | Prazo Contratual           |                     4 |
      | Objeto                     | Esgotamento sanitário |
      | Área de Atuação            | Urbano                |
      | data da primeira Concessão | 12/05/2016            |
      | Observação do Contrato     | observação            |

  @Functional
  Esquema do Cenario: US001 - Incluir um Instrumento Contratual com Prazo contratual dentro do formato válido
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E seleciono o prazo contratual em <OPCAO_TEMPO>
    E informo o valor do prazo contratual com <VALOR_PRAZO_CONTRATUAL>
    Quando clicar em salvar
    Então Instrumento contratual deve ser salvo

    Exemplos: 
      | OPCAO_TEMPO | VALOR_PRAZO_CONTRATUAL |
      | ANO         |                      1 |
      | ANO         |                     10 |
      | ANO         |                 999999 |
      | MES         |                      1 |
      | MES         |                     10 |
      | MES         |                 999999 |
      | DIA         |                      1 |
      | DIA         |                     10 |
      | DIA         |                 999999 |

  @Functional
  Esquema do Cenario: US001 - Incluir um Instrumento Contratual com Prazo contratual igual a zero
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E seleciono o prazo contratual em <OPCAO_TEMPO>
    Quando informo o valor zero
    Então não será possível informar o valor

    Exemplos: 
      | OPCAO_TEMPO |
      | ANO         |
      | MES         |
      | DIA         |

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual  com data de assinatura maior que a data atual
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E informo a data de assinatura maior que a data atual
    Quando clicar em salvar
    Entao nao deve incluir o Instrumento Contratual
    E deve ser exibida a seguinte mensagem: "Data da assinatura maior que a data do dia"

  @Functional
  Cenario: US001 - Incluir um Instrumento Contratual com  data da primeira Concessão maior que a data atual
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E informo a data da primeira Concessão maior que a data atual
    Quando clicar em salvar
    Entao nao deve incluir o Instrumento Contratual
    E deve ser exibida a seguinte mensagem: "Data da primeira concessão maior que a data do dia"

  @Functional
  Esquema do Cenario: US001 - Incluir um Instrumento Contratual com numero de contrato igual entre dois usuários
    Dado dois Instrumentos Contratual sendo acessados ao mesmo tempo
    E um Instrumento Contratual com os campos obrigat�rios preenchidos
    E o número de contrato <NUMERO DO CONTRATO>
    E o tipo de contrato <TIPO DO CONTRATO>
    E que o ultimo numero de contrato incluido foi o <NUMERO DO CONTRATO INCLUIDO> para o tipo <TIPO DO CONTRATO INCLUIDO>
    Quando clicar em salvar
    Entao nao deve incluir o Instrumento Contratual
    E deve ser exibida a seguinte mensagem "Contrato já existe no sistema. Novo número de contrato: xx.xx9/x9"

    Exemplos: 
      | NUMERO DO CONTRATO | TIPO DO CONTRATO     | NUMERO DO CONTRATO INCLUIDO | TIPO DO CONTRATO INCLUIDO |
      |                123 | CONTRATO DE PROGRAMA |                         123 | CONTRATO DE PROGRAMA      |
      |                123 | CONCESSAO            |                         123 | CONCESSAO                 |

  @Functional
  Esquema do Cenário: US001 - Upload de arquivos
    Dado um Instrumento Contratual com os campos obrigatórios preenchidos
    E anexei um arquivo <NOME_DO_DOCUMENTO> para o documento do tipo <TIPO_DE_DOCUMENTO>
    Quando clicar em salvar
    Então Instrumento contratual deve ser salvo
    E Instrumento contratual deve estar com a situação "Em contratação"
    E devo receber a mensagem: "Registro incluído com sucesso"
    E documento é salvo no Alfresco

    Exemplos: 
      | NOME_DO_DOCUMENTO | TIPO_DE_ARQUIVO |
      | Licitacao         | Pesquisável     |
      | Decreto           | Imagem          |

  @Functional
  Esquema do Cenário: US001 - Acessar pop-up para anexar um documento
    Dado que estou no formulário de incluir Instrumento contratual
    Quando clicar no botão carregar de campo <TIPO_DE_DOCUMENTO>
    Então uma pop-up é exibida
    E é possível selecionar um arquivo para ser associado

    Exemplos: 
      | TIPO_DE_DOCUMENTO     |
      | Documento imagem      |
      | Documento Pesquisável |

  @Functional
  Cenário: US001 - Geração de dados para auditoria de incluir Instrumento Contratual
    Dado um instrumento contratual com pelo menos os campos obrigatórios preenchidos
    Quando clicar em salvar
    Então instrumento contratual deve ser salvo
    E deve ser disparado a geração de dados de auditoria para todos os campos preenchidos

  @Functional
  Cenário: US001 - Incluir um documento do tipo Pesquisável com extensão diferente de .PDF
    Dado que estou na pop-up para incluir um arquivo do tipo pesquisável
    Quando associar um arquivo com extensão diferente de .PDF
    Então arquivo não deve ser associado
    E devo receber a seguinte mensagem: "Arquivo pesquisável deve ter extensão .PDF"

  @Functional
  Esquema do Cenário: US001 -	Não permitir salvar o instrumento contratual quando município já possui outro contrato na mesma situação
    Dado que estou no formulário de incluir Instrumento contratual com situação "Em Contratação"
    E município <NOME_DO_MUNICIPIO> possuí um instrumento contratual com a situação "Em Contratação"
    E incluí o valor do campo município para <NOVO_VALOR_MUNICIPIO>
    Quando clicar em salvar
    Então devo visualizar a mensagem "Município já possui contrato com a situação Em Contratação"
    E inclusão não deve ser salva

    Exemplos: 
      | NOME_DO_MUNICIPIO | NOVO_VALOR_MUNICIPIO |
      | Alegrete          | Alegrete             |
      | Santa Maria       | Santa Maria          |

  @Functional
  Esquema do Cenário: US001 - Verificar disponibilidade do número digitado
    Dado que estou na tela de incluir Instrumento Contratual
    E ultimo contrato do tipo <TIPO_DE_CONTRATO> criado teve o valor <VALOR_CONTRATO>
    E preenchi o campo Nº do Contrato com um valor existente
    Quando clicar em salvar
    Então devo visualizar a seguinte mensagem: "Contrato já existe no sistema. Novo número de contrato: "<NOVO_VALOR_CONTRATO>"

    Exemplos: 
      | TIPO_DE_CONTRATO     | VALOR_CONTRATO | NOVO_VALOR_CONTRATO |
      | Contrato de Programa |             10 | 11/0                |
      | Contrato de Programa |            100 | 101/0               |
      | Concessão            |             15 | 16/0                |

  @Functional
  Esquema do Cenário: US001 - Validar documento de origem de acordo com o tipo de contrato
    Dado que estou na tela incluir contrato com poder concedente
    E possuo um contrato do tipo <TIPO_DO_CONTRATO>
    Quando clicar em Salvar
    Então deve ser vinculado o <DOCUMENTO_DE_ORIGEM> indicado no código pelo seu identificador
    E que esteja dentro da vigência

    Exemplos: 
      | TIPO_DO_CONTRATO     | DOCUMENTO_DE_ORIGEM |
      | Contrato de Programa |                   1 |
      | Concessão            |                   2 |

  @Functional
  Cenário: US001 - Verificar campo convênio de delegação
    Dado que estou na tela de incluir Instrumento Contratual
    E contrato possui situação diferente de "Contrato Encerrado"
    Quando visualizar o campo "Convênio de delegação"
    Então devo visualizar o nome da Agência Reguladora do Convênio de delegação que está ativo, que possua a maior data de vencimento, para município do contrato

  @Functional
  Cenário: US001 - Verificar campo convênio de delegação com convênio vencido
    Dado que estou na tela de incluir Instrumento Contratual
    E convênio de delegação estiver vencido
    Quando visualizar o campo "Convênio de delegação"
    Então devo visualizar a seguinte mensagem: "Convênio de delegação está vencido: dd/mm/yyyy"
    E a data de vencimento deve estar em vermelho
