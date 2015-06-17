<script language="VBScript">
Dim gConfiguracao
Dim gCertificadoB64
Dim gUtil
Dim gAssinatura
Dim gAtributoAssinavelDataHora

Function assinar()
	Dim Assinatura
	Dim Configuracao
	On Error Resume Next
	Set Configuracao = CreateObject("CAPICOM.Settings")
	Configuracao.EnablePromptForCertificateUI = True
	Set Assinatura = CreateObject("CAPICOM.SignedData")
	Set Util = CreateObject("CAPICOM.Utilities")
	If Erro Then Exit Function
	Assinatura.Content = Util.Base64Decode(frm.conteudo_b64.value)
	frm.assinaturaB64.value = Assinatura.Sign(Nothing, True, 0)
	If Erro Then Exit Function
	Dim Assinante
	Assinante = Assinatura.Signers(1).Certificate.SubjectName
	Assinante = Split(Assinante, "CN=")(1)
	Assinante = Split(Assinante, ",")(0)
	frm.assinante.value = Assinante
	frm.conteudo_b64.value = ""
	If Erro Then Exit Function
	frm.Submit()
End Function

Function Erro() 
	If Err.Number <> 0 then
		MsgBox "Ocorreu um erro durante o processo de assinatura: " & Err.Description
		Err.Clear
		Erro = True
	Else
		Erro = False
	End If
End Function

Function InicializarCapicom()
	Dim Desprezar
	On Error Resume Next
	Set gConfiguracao = CreateObject("CAPICOM.Settings")
	gConfiguracao.EnablePromptForCertificateUI = True
	Set gAssinatura = CreateObject("CAPICOM.SignedData")
	Set gUtil = CreateObject("CAPICOM.Utilities")
	If Erro Then Exit Function

	'Infelizmente não é possível armazenar o objeto Signer e utilizá-lo na próxima chamada para Sign.
	'Renato: desabilitado temporariamente para não interferir com os testes de desempenho.
	gAssinatura.Content = "Produz assinatura apenas para identificar o certificado a ser utilizado."
	Desprezar = gAssinatura.Sign(Nothing, True, 0)
	gCertificadoB64 = gAssinatura.Signers(1).Certificate.Export(0)

	InicializarCapicom = "OK"
End Function

Function AssinarDocumento(conteudo, ByRef assinante, ByRef assinaturaB64)
	On Error Resume Next
	gAssinatura.Content = Conteudo
	assinaturaB64 = gAssinatura.Sign(Nothing, True, 0)
	If Erro Then Exit Function
	assinante = gAssinatura.Signers(1).Certificate.SubjectName
	assinante = Split(assinante, "CN=")(1)
	assinante = Split(assinante, ",")(0)
	If Erro Then Exit Function
	AssinarDocumento = "OK"
End Function

Dim intID ' set a global variable
Function AssinarDocumentos(Copia, oElm)
	TestCAPICOM
	If InicializarCapicom() <> "OK" Then
        MsgBox "Erro na inicializacao da CAPICOM.", 0, "Não foi possível completar a operação"
		Exit Function
    End If

	Dim Id, Caption
	If Not IsEmpty(oElm) Then
		Id = oElm.id
		Caption = oElm.InnerHTML
		oElm.InnerHTML = "Aguarde..." 
		'MsgBox oElm.id
    End If
	intID = window.setInterval("AssinarDocumentosAgora """ + Copia + """, """ + Id + """, """ + Caption + """", 1000, "vbscript") 
End Function

Function AssinarDocumentosAgora(Copia, Id, Caption)
    Window.clearInterval intID 
    Set oElm = Document.getElementByID(Id)
    oElm.InnerHTML = Caption
    If Copia = "true" Then
		Copia = "true"
'	    MsgBox "Iniciando conferência"
	    Log "Iniciando conferência"
	Else
		Copia = "false"
'	    MsgBox  "Iniciando assinatura"
	    Log "Iniciando assinatura"
    End If

    Dim oUrlPost, oNextUrl, oUrlBase, oUrlPath, oNome, oUrl, oChk
 
    Set oUrlPost = document.getElementById("jspserver")
    If oUrlPost Is Nothing Then
        MsgBox("element jspserver does not exist")
        Exit Function
    End If
    Set oUrlNext = document.getElementById("nexturl")
    If oUrlNext Is Nothing Then
        MsgBox("element nexturl does not exist")
        Exit Function
    End If
    Set oUrlBase = document.getElementById("urlbase")
    If oUrlBase Is Nothing Then
        MsgBox("element urlbase does not exist")
        Exit Function
    End If
    Set oUrlPath = document.getElementById("urlpath")
    If oUrlPath Is Nothing Then
        MsgBox("element urlpath does not exist")
        Exit Function
    End If

	Dim Codigo
	Set NodeList = Document.getElementsByTagName("INPUT")
    For Each Elem In NodeList
       If Left(Elem.name, 7) = "pdfchk_" Then
           Codigo = Mid(Elem.name, 8)
           'MsgBox Codigo

           Set oNome = document.getElementsByName("pdfchk_" & Codigo).Item(0)
           If oNome Is Nothing Then
               MsgBox("element pdfchk_" & Codigo & " does not exist")
               Exit Function
           End If
           Set oUrl = document.getElementsByName("urlchk_" & Codigo).Item(0)
           Set oChk = document.getElementsByName("chk_" & Codigo).Item(0)

		   Dim b
           If oChk Is Nothing Then
               b = True
           Else
               b = oChk.Checked
           End If 

           If b Then
               Dim urlDocumento, Documento
 			   Dim certParam

               urlDocumento = oUrlBase.value + oUrlPath.value + oUrl.value + "&semmarcas=1"
			   If Not isEmpty(gCertificadoB64) Then
			       certParam = "certificadoB64=" & UrlEncode(gCertificadoB64)
                   urlDocumento = urlDocumento & "&" & certParam
               End If

               Documento = Conteudo(urlDocumento)
               'MsgBox Documento
               Log "Documento: " & oNome.value
       
               Dim Status, Assinante, AssinaturaB64, DadosDoPost
               Status = AssinarDocumento(Documento, Assinante, AssinaturaB64)	
               Log "Documento: " & oNome.value & ", Assinante: " & Assinante
       
               DadosDoPost = "sigla=" & UrlEncode(oNome.value) & "&copia=" & Copia & "&assinaturaB64=" & UrlEncode(AssinaturaB64) & "&assinante=" + UrlEncode(Assinante)
			   If Not isEmpty(gCertificadoB64) Then
                   DadosDoPost = DadosDoPost & "&" & certParam
                   DadosDoPost = DadosDoPost & "&atributoAssinavelDataHora=" & gAtributoAssinavelDataHora
               End If

			   'MsgBox "oNome: " & oNome.value
               Dim aNome
               aNome = Split(oNome.value, ":")
               If UBound(aNome) = 1 Then
				   'MsgBox "id: " & aNome(1)
                   DadosDoPost = "id=" & aNome(1) & "&" & DadosDoPost
               End If

			   Log "Documento: " & oNome.value & ", Gravando..."
               Status = GravarAssinatura(oUrlPost.value, DadosDoPost)
               If Status = "OK" Then
                   Log "Documento: " & oNome.value & ", OK, Gravado!"
               Else
		            MsgBox Status, 0, "Não foi possível completar a operação"
					Exit Function
               End If
           End If
       End If
    Next

	If Status = "OK" Then
		'MsgBox "Redirecionando para " & oUrlNext.value
        Log "Concluído, redirecionando..."
		Location.href = oUrlNext.value
    End If
End Function

Function Conteudo(url)
	'MsgBox url
	Set objHTTP = CreateObject("MSXML2.XMLHTTP")
	objHTTP.open "GET", url, False
	objHTTP.send

	If objHTTP.Status = 200 Then
		gAtributoAssinavelDataHora = objHTTP.getResponseHeader("Atributo-Assinavel-Data-Hora")
		Conteudo = objHTTP.responseBody
	End If
End Function

Function GravarAssinatura(url, datatosend)
	'MsgBox "Enviando: " & url
	Set objHTTP = CreateObject("MSXML2.XMLHTTP")
	objHTTP.Open "POST", url,false
	objHTTP.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
	objHTTP.send datatosend 

	GravarAssinatura = "Erro inespecífico."
	If objHTTP.Status = 200 Then
        Dim Conteudo, Inicio, Fim, Texto
		'MsgBox "OK, enviado"
		GravarAssinatura = "OK"
		Conteudo = objHTTP.responseText

        If InStr(Conteudo, "gt-error-page-hd") > 0 Then
			Inicio = InStr(Conteudo, "<h3>") + 4
			Fim = InStr(Inicio,Conteudo, "</h3>")
            Texto = Mid(Conteudo, Inicio, Fim - Inicio)
			GravarAssinatura = Texto
        End If
	End If
End Function

Function URLEncode(ByVal Text)
    Dim i
    Dim acode
    Dim char
    
    URLEncode = Text
    
    For i = Len(URLEncode) To 1 Step -1
        acode = Asc(Mid(URLEncode, i, 1))
        Select Case True
            Case (acode >= 48 And acode <= 57) Or (acode >= 65 And acode <= 90), (acode >= 97 And acode <= 122)
                ' don't touch alphanumeric chars
            Case (acode = 32)
                ' replace space with "+"
                URLEncode = Left(URLEncode, i - 1) & "+" & Mid(URLEncode, i + 1)
            Case Else
                ' replace punctuation chars with "%hex"
                If acode < 16 Then
                    URLEncode = Left(URLEncode, i - 1) & "%0" & Hex(acode) & Mid(URLEncode, i + 1)
                Else
                    URLEncode = Left(URLEncode, i - 1) & "%" & Hex(acode) & Mid(URLEncode, i + 1)
                End If
        End Select
    Next
End Function

'Dim intID ' set a global variable
 
Sub Log(Text)
    Dim oLog
    Set oLog = document.getElementById("vbslog")
    If Not oLog Is Nothing Then
    	oLog.InnerHTML = Text
		'intID = window.setInterval("clearIntvl", 1000, "vbscript") 
    End If
End Sub


'Sub clearIntvl 
'    Window.clearInterval intID 
'End Sub

Sub TestCAPICOM
    On Error Resume Next
	Err.Clear
    Set oTest = CreateObject("CAPICOM.Settings")
	If  Err.Number <> 0  Then   
		On Error GoTo 0
        Set oMissing = document.getElementById("capicom-missing")
        If Not (oMissing Is Nothing) Then
            oMissing.style.display = "block"
        End If
        Set oDiv = document.getElementById("capicom-div")
        If Not (oDiv Is Nothing) Then
            oDiv.style.display = "none"
        End If
    End If
End Sub
</script>
