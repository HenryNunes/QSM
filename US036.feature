# language: pt
# encoding: iso-8859-1
Funcionalidade: US036 - Incluir Negociação
  Como um Administrador ou Analista SUPRIN
	Eu quero incluir uma negociação no sistema para o município
	Para que seja possível incluir os documentos recebidos para esta negociação.

  @Functional
  Esquema do Cenário: US036 - Validar acesso a funcionalidade de incluir negociação
    Dado que possuo o seguinte perfil <PERFIL>
    Quando acessar o sistema
    Então devo poder acessar a tela de incluir negociação

    Exemplos: 
      | PERFIL          |
      | Administrador   |
      | Analista SUPRIN |

  @Functional
  Esquema do Cenário: US036 - Não permitir acesso a funcionalidade incluir negociação para perfil diferente de Administrador ou Analista Suprin
    Dado que possuo o seguinte perfil <PERFIL>
    Quando acessar o sistema
    Então não devo poder acessar a tela de incluir Negociação

    Exemplos: 
      | PERFIL             |
      | Gestão de Demandas |
  
  @Functional
  Cenário: US036 - Acessar o formulario de Negociação
    Dado que estou acessando o Sisplan
    E estou na tela de lista de negociações
    Quando clicar em Novo
    Entao formulario de Negociação de contrato deve ser exibido
    E formulario deve conter os seguintes campos obrigatorios
      | campo obrigatórios |
      | município          |
      | data da negociação |
      | tipo de contrato   |
      | situação           |
    E os seguintes botoes: Salvar e Fechar

  @Integration
  Cenario: US036 - Incluir uma Negociação com sucesso
    Dado uma negociação com os campos obrigatórios preenchidos
    Quando clicar em salvar
    Então negociação deve ser salva
    E devo receber a seguinte mensagem: "Registro incluído com sucesso."
    E negociação será incluída no sistema com a situação “Em negociação"
    E devo ser redirecionada para a tela de alterar negociação
    E o sistema deve gerar, automaticamente, um acompanhamento da contratação com o assunto "Inclusão"

  @Functional
  Cenário: US036 - Valor default Contrato de Programa no campo Tipo de contrato
    Dado que estou na tela de lista de negociações
    Quando clicar em novo
    Então formulário de Negociação de contrato deve ser exibido
    E o campo tipo de contrato deve vir com o valor padrão: Contrato de Programa

  @Functional
  Cenário: US036 - Não incluir uma Negociação sem os campos obrigatórios preenchidos
    Dado que estou no formulário de negociações
    E não preenchi todos os campos obrigatórios
    Quando clicar em salvar
    Então negociação não deve ser salva
    E devo receber uma alerta para preencher os campos obrigatórios

  @Integration
  Cenário: US036 - Incluir uma Negociação com data de negociação maior que a data atual do sistema
    Dado que estou no formulário de negociação de abertura
    E inseri a data de negociação com valor maior que a data atual do sistema
    Quando clicar em salvar
    Então negociação não deve ser salva
    E deve ser exibida a seguinte mensagem: "Data da negociação maior que a data do dia"

  @Integration
#  Esquema do Cenário: US036 - Não incluir uma Negociação com município que já possui uma negociação com a situação "Em negociação"
#    Dado que já existe uma negociação cadastrada para o município <NOME_DO_MUNICÍPIO>
#    E preenchi o fomrulário de Negociações para o municpio <NOME_DO_MUNICÍPIO>
#    Quando clicar em Salvar
#    Então deve ser exibida a seguinte mensagem: "Município já possui uma negociação na situação 'Em negociação'."
#    E negociação não deve ser salva

#    Exemplos: 
#      | NOME_DO_MUNICÍPIO |
#      | Aceguá            |
#      | Agua Santa        |

  @Functional
  Cenário: US036 - Clicar no botão fechar
    Dado que estou no formulário de negociação
    Quando clicar no botão fechar
    Então tela de inclusão é fechada
    E devo ser redirecionada para a tela de Pesquisar negociação

  @Functional
  Cenário: US036 - Geração de dados para auditoria
    Dado uma negociação com os campos obrigatórios preenchidos
    Quando clicar em salvar
    Então negociação deve ser salva
    E negociação será incluída no sistema com a situação “Em negociação"
    E deve ser gerado dados de auditoria dos campos: município, data da negociação, tipo de contrato e situação
    
	
  @Functional
  Esquema do Cenario: US036 - Validar documento de origem de acordo com o tipo de contrato
		Dado que estou na tela incluir negociação
		E possuo um contrato do tipo <TIPO_DO_CONTRATO>
		Quando clicar em Salvar
		Então deve ser vinculado o <DOCUMENTO_DE_ORIGEM> indicado no código pelo seu identificador 
		E que esteja dentro da vigência
		
		Exemplos:
		| TIPO_DO_CONTRATO | DOCUMENTO_DE_ORIGEM |
		| Negociação       | 3                   |
		
		
  @Functional
	Cenário: US036 - Incluir evento automático para Acompanhamento da Contratação
		Dado que estou na tela Incluir Negociação
		Quando clicar no botão "Salvar"
		Então o sistema deve gerar, automaticamente, um evento na lista acompanhamento da contratação com o assunto "Inclusão" na categoria "Município"
		E categoria Município que esteja dentro da vigência||||||| .merge-left.r5393
    
#  @Functional
#	Cenário: US036 - Incluir evento automático para Acompanhamento da Contratação
#		Dado que estou na tela Incluir Negociação
#		Quando clicar no botão "Salvar"
#		Então o sistema deve gerar, automaticamente, um acompanhamento da contratção com o assunto "Inclusão"=======
#    >>>>>>> .merge-right.r5394
