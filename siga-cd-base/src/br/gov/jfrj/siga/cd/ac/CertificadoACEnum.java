/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.cd.ac;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

/**
 * Enumerador de Certificados de Autoridades Certificadoras (AC)
 * 
 * Sempre que um novo certificado deva ser tratado pelo(s) sistema(s) que
 * utilizam o siga-cd deva ser considerado, o mesmo deve ser inserido neste
 * enumerador
 * 
 * @author aym
 * 
 */
public enum CertificadoACEnum {

	AC_RAIZ_ICPBRASIL_ERRADO(
			"AC_RAIZ_ICPBRASIL_ERRADO",
			Base64
					.decode("MIIEPjCCAyagAwIBAgIBEDANBgkqhkiG9w0BAQUFADCBtDELMAkGA1UEBhMCQlIxEzARBgNVBAoT"
							+ "CklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBkZSBUZWNub2xvZ2lhIGRh"
							+ "IEluZm9ybWFjYW8gLSBJVEkxETAPBgNVBAcTCEJyYXNpbGlhMQswCQYDVQQIEwJERjExMC8GA1UE"
							+ "AxMoQXV0b3JpZGFkZSBDZXJ0aWZpY2Fkb3JhIFJhaXogQnJhc2lsZWlyYTAeFw0wNDExMjYxMjIw"
							+ "MDBaFw0xMTExMjYyMzU5MDBaMFYxCzAJBgNVBAYTAkJSMRMwEQYDVQQKEwpJQ1AtQnJhc2lsMRgw"
							+ "FgYDVQQLEw9BQyBDZXJ0aVNpZ24gVjMxGDAWBgNVBAMTD0FDIENlcnRpU2lnbiBWMzCCASIwDQYJ"
							+ "KoZIhvcNAQEBBQADggEPADCCAQoCggEBAK17G0+cTG8qjqwPidslvptoaCpUCEZn+Xp3sggJVeN/"
							+ "A7JVccy1hXum9/uJX2nvlNbXu0G/nmADwHP7YeunPtkk3Ah2IuFHPU/f1KodJI9IbX7qLPuEoBtr"
							+ "od0g7uE2XPBnSjxIFMwtpIs1Sp9AQSzaAezq63gKP9qkvIKiAB3r0rwo5jRP9Tey8oo79OPcNHam"
							+ "7l0DfkKutSaO/oCgWyMunYmmPxx56dUTQWz0HFsLiRnetve4EHisH8vfPmBpaTQY845120K4MofB"
							+ "dpzSb7Vj4j5WrStx/+5+r/gNcWDY9pJa9a9M5Sy5rvPA4gdgK8TbfRir00mZ0qF7cOr4EW0CAwEA"
							+ "AaOBtzCBtDA9BgNVHR8ENjA0MDKgMKAuhixodHRwOi8vYWNyYWl6LmljcGJyYXNpbC5nb3YuYnIv"
							+ "TENSYWNyYWl6LmNybDASBgNVHSAECzAJMAcGBWBMAQEFMB0GA1UdDgQWBBRkXeNVqD8sfj2lpUVc"
							+ "h/+6cUXWwDAfBgNVHSMEGDAWgBSK+vFXhBETNZBC+ldJVGkNpMTwNzAOBgNVHQ8BAf8EBAMCAQYw"
							+ "DwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQUFAAOCAQEAB4oeFtBfWe3eN57jMjD+gfpk/PGd"
							+ "QTs67/njYGSeaowwMcdrS7/Hb0FvjcEB4FmMAFyXOFK7XtBEMoHKRdu4X655LlzemvB+euWkJZ8r"
							+ "M8WWVvQKsKKEOrh+tCWNhpdYFQ91lKKu5kkAlabmD0G4bXfLYX5i0ypNXw/16n7NNMfKO1rrVxp1"
							+ "k7gmDcsBOXl7787yVFY/yt4vr9Ok28fB6P4c0u8t7jVjSQ5gFUXOWlv1DgstfX+Nv3hmoX3AzCrO"
							+ "Hs8cSAEKnhcRwzA/nsJN4+zoSqpYT/171XFD50TUtrjSVgHOrPMnKGS1sByYx5gzonchTO9zi1f6"
							+ "G1ZAlMOHCw==")), AC_RAIZ_ICPBRASIL(
			"AC_RAIZ_ICPBRASIL",
			Base64
					.decode("MIIEuDCCA6CgAwIBAgIBBDANBgkqhkiG9w0BAQUFADCBtDELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxETAPBgNVBAcTCEJy"
							+ "YXNpbGlhMQswCQYDVQQIEwJERjExMC8GA1UEAxMoQXV0b3JpZGFkZSBDZXJ0aWZp"
							+ "Y2Fkb3JhIFJhaXogQnJhc2lsZWlyYTAeFw0wMTExMzAxMjU4MDBaFw0xMTExMzAy"
							+ "MzU5MDBaMIG0MQswCQYDVQQGEwJCUjETMBEGA1UEChMKSUNQLUJyYXNpbDE9MDsG"
							+ "A1UECxM0SW5zdGl0dXRvIE5hY2lvbmFsIGRlIFRlY25vbG9naWEgZGEgSW5mb3Jt"
							+ "YWNhbyAtIElUSTERMA8GA1UEBxMIQnJhc2lsaWExCzAJBgNVBAgTAkRGMTEwLwYD"
							+ "VQQDEyhBdXRvcmlkYWRlIENlcnRpZmljYWRvcmEgUmFpeiBCcmFzaWxlaXJhMIIB"
							+ "IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwPMudwX/hvm+Uh2b/lQAcHVA"
							+ "isamaLkWdkwP9/S/tOKIgRrL6Oy+ZIGlOUdd6uYtk9Ma/3pUpgcfNAj0vYm5gsyj"
							+ "Qo9emsc+x6m4VWwk9iqMZSCK5EQkAq/Ut4n7KuLE1+gdftwdIgxfUsPt4CyNrY50"
							+ "QV57KM2UT8x5rrmzEjr7TICGpSUAl2gVqe6xaii+bmYR1QrmWaBSAG59LrkrjrYt"
							+ "bRhFboUDe1DK+6T8s5L6k8c8okpbHpa9veMztDVC9sPJ60MWXh6anVKo1UcLcbUR"
							+ "yEeNvZneVRKAAU6ouwdjDvwlsaKydFKwed0ToQ47bmUKgcm+wV3eTRk36UOnTwID"
							+ "AQABo4HSMIHPME4GA1UdIARHMEUwQwYFYEwBAQAwOjA4BggrBgEFBQcCARYsaHR0"
							+ "cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJyL0RQQ2FjcmFpei5wZGYwPQYDVR0f"
							+ "BDYwNDAyoDCgLoYsaHR0cDovL2FjcmFpei5pY3BicmFzaWwuZ292LmJyL0xDUmFj"
							+ "cmFpei5jcmwwHQYDVR0OBBYEFIr68VeEERM1kEL6V0lUaQ2kxPA3MA8GA1UdEwEB"
							+ "/wQFMAMBAf8wDgYDVR0PAQH/BAQDAgEGMA0GCSqGSIb3DQEBBQUAA4IBAQAZA5c1"
							+ "U/hgIh6OcgLAfiJgFWpvmDZWqlV30/bHFpj8iBobJSm5uDpt7TirYh1Uxe3fQaGl"
							+ "YjJe+9zd+izPRbBqXPVQA34EXcwk4qpWuf1hHriWfdrx8AcqSqr6CuQFwSr75Fos"
							+ "SzlwDADa70mT7wZjAmQhnZx2xJ6wfWlT9VQfS//JYeIc7Fue2JNLd00UOSMMaiK/"
							+ "t79enKNHEA2fupH3vEigf5Eh4bVAN5VohrTm6MY53x7XQZZr1ME7a55lFEnSeT0u"
							+ "mlOAjR2mAbvSM5X5oSZNrmetdzyTj2flCM8CC7MLab0kkdngRIlUBGHF1/S5nmPb"
							+ "K+9A46sd33oqK8n8")), AC_RAIZ_ICPBRASIL_V1(
			"AC_RAIZ_ICPBRASIL_V1",
			Base64
					.decode("MIIEgDCCA2igAwIBAgIBATANBgkqhkiG9w0BAQUFADCBlzELMAkGA1UEBhMCQlIxEzARBgNVBAoT"
							+ "CklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBkZSBUZWNub2xvZ2lhIGRh"
							+ "IEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6"
							+ "IEJyYXNpbGVpcmEgdjEwHhcNMDgwNzI5MTkxNzEwWhcNMjEwNzI5MTkxNzEwWjCBlzELMAkGA1UE"
							+ "BhMCQlIxEzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBk"
							+ "ZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1dG9yaWRhZGUgQ2Vy"
							+ "dGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK"
							+ "AoIBAQDOHOi+kzTOybHkVO4J9uykCIWgP8aKxnAwp4CM7T4BVAeMGSM7n7vHtIsgseL3QRYtXodm"
							+ "urAH3W/RPzzayFkznRWwn5LIVlRYijonojQem3i1t83lm+nALhKecHgH+o7yTMD45XJ8HqmpYANX"
							+ "Jkfbg3bDzsgSu9H/766zYn2aoOS8bn0BLjRg3IfgX38FcFwwFSzCdaM/UANmI2Ys53R3eNtmF9/5"
							+ "Hw2CaI91h/fpMXpTT89YYrtAojTPwHCEUJcV2iBL6ftMQq0raI6j2a0FYv4IdMTowcyFE86tKDBQ"
							+ "3d7AgcFJsF4uJjjpYwQzd7WAds0qf/I8rF2TQjn0onNFAgMBAAGjgdQwgdEwTgYDVR0gBEcwRTBD"
							+ "BgVgTAEBADA6MDgGCCsGAQUFBwIBFixodHRwOi8vYWNyYWl6LmljcGJyYXNpbC5nb3YuYnIvRFBD"
							+ "YWNyYWl6LnBkZjA/BgNVHR8EODA2MDSgMqAwhi5odHRwOi8vYWNyYWl6LmljcGJyYXNpbC5nb3Yu"
							+ "YnIvTENSYWNyYWl6djEuY3JsMB0GA1UdDgQWBBRCsixcdAEHvpv/VTM77im7XZG/BjAPBgNVHRMB"
							+ "Af8EBTADAQH/MA4GA1UdDwEB/wQEAwIBBjANBgkqhkiG9w0BAQUFAAOCAQEAWWyKdukZcVeD/qf0"
							+ "eg+egdDPBxwMI+kkDVHLM+gqCcN6/w6jgIZgwXCX4MAKVd2kZUyPp0ewV7fzq8TDGeOY7A2wG1GR"
							+ "ydkJ1ulqs+cMsLKSh/uOTRXsEhQZeAxi6hQ5GArFVdtThdx7KPoVcaPKdCWCD2cnNNeuUhMC+8Xv"
							+ "moAlpVKeOQ7tOvR4B1/VKHoKSvXQw2f3jFgXbwoAoyYQtGAiOkpIpdrgqYTeQ9ufQ6c/KARHki/3"
							+ "52R1IdJPgc6qPmQO4w6tVZp+lJs0wdCuaU4eo9mzh1facMJafYfN+b833u1WNfe3Ig5Pkrg/CN+c"
							+ "nphe8m+5+pss+M1F2HKyIA==")), AC_CLEPSIDRE_TIME_STAMP(
			"AC_CLEPSIDRE_TIME_STAMP",
			Base64
					.decode("MIIDYDCCAkigAwIBAgIIAQI3JEZlEXgwDQYJKoZIhvcNAQEFBQAwcDELMAkGA1UEBhMCRlIxFTAT"
							+ "BgNVBAoTDEVkZWxXZWIgUy5BLjEoMCYGA1UECxMfQ2xlcHN5ZHJlIERlbW9uc3RyYXRpb24gU2Vy"
							+ "dmljZTEgMB4GA1UEAxMXVGltZSBTdGFtcGluZyBBdXRob3JpdHkwHhcNMDIwNjEwMTU1NDMyWhcN"
							+ "MjIwNjA1MTU1NDMyWjBwMQswCQYDVQQGEwJGUjEVMBMGA1UEChMMRWRlbFdlYiBTLkEuMSgwJgYD"
							+ "VQQLEx9DbGVwc3lkcmUgRGVtb25zdHJhdGlvbiBTZXJ2aWNlMSAwHgYDVQQDExdUaW1lIFN0YW1w"
							+ "aW5nIEF1dGhvcml0eTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAPrDF67rt53rq70F"
							+ "fjlDbQRFWHQFpczzbC+Mjnd+wp8SEVx9274jKJqQ0qvGorq9o36ZppkhpdiQuc+nI06gVqDBCkaJ"
							+ "jjyRZzf9m6tJF/xKpfLkTG7jahySlwRvfwxc+3TLlX5Mw1gS6KnW8N0SRBXniy6vUcAMX6hl/Eeh"
							+ "yZgf1OHqvBwaJ7uLVvESVRD0jtifGZwegffbY92INz9xeVuW4l+C1RIZBQ3hPaVtZuQsHu3HTLjf"
							+ "qjjIFWquJX1GKgf5g3fEUe6Q3AXQw/DxX+jU7V00cJGdnwhVfVvljV81WYNOchm7nIjRevwjpYSZ"
							+ "tBeKTWyd0KY1gF/K+ySLVB0CAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAvSFryCfr5SdnJz8w5vYU"
							+ "oSQZSVJ3kkRzkTXG9058zG+ND5xVoxhjTvrhUM9klOFfOsy8HFeqNSO2OfN8uOCXD3VxVV/bMGpm"
							+ "lOX0igNJWbvivv7FbkaRVsFP5BWCrBuML9xkd/H53O1eJo0RhstQPOZ8A0bOlkCsOnq+33vKJ3er"
							+ "ABuKdg3BjQVI4pDBgIBxnGUjizw/+N4n3dq6WD2Xiz5T3HYZ3DkbuzCg0I/SlM79d+kkbiuPesea"
							+ "LRZoDtxylK2Ul7sxdAZVihNQ4wVaEDulY7ZNtDNYeV4/lnqGKLB6zB9IFcjn5hNPB0EfLBqbeWkk"
							+ "P6FmoXNdZPAoqD6DPg==")), AC_SZIKSZI_TIME_STAMP(
			"AC_SZIKSZI_TIME_STAMP",
			Base64
					.decode("MIIDUzCCArygAwIBAgIBBDANBgkqhkiG9w0BAQUFADCBvzELMAkGA1UEBhMCQVQx"
							+ "DzANBgNVBAgTBlN0eXJpYTENMAsGA1UEBxMER3JhejEmMCQGA1UEChMdR1JBWiBV"
							+ "TklWRVJTSVRZIE9GIFRFQ0hOT0xPR1kxSDBGBgNVBAsTP0luc3RpdHV0ZSBmb3Ig"
							+ "QXBwbGllZCBJbmZvcm1hdGlvbiBQcm9jZXNzaW5nIGFuZCBDb21tdW5pY2F0aW9u"
							+ "czEeMBwGA1UEAxMVSUFJSy1UU1AgREVNTyBTZXJ2aWNlMB4XDTA4MDMwNTA5MTA1"
							+ "MloXDTE2MDMwNTA5MTA1Mlowgb8xCzAJBgNVBAYTAkFUMQ8wDQYDVQQIEwZTdHly"
							+ "aWExDTALBgNVBAcTBEdyYXoxJjAkBgNVBAoTHUdSQVogVU5JVkVSU0lUWSBPRiBU"
							+ "RUNITk9MT0dZMUgwRgYDVQQLEz9JbnN0aXR1dGUgZm9yIEFwcGxpZWQgSW5mb3Jt"
							+ "YXRpb24gUHJvY2Vzc2luZyBhbmQgQ29tbXVuaWNhdGlvbnMxHjAcBgNVBAMTFUlB"
							+ "SUstVFNQIERFTU8gU2VydmljZTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA"
							+ "6dq6Fox+glq0Q5Vr9wnlwcqNkVCkqxjjXpSaxBZx9eBsVz94sN2IjxPOAvmrBkIS"
							+ "qc0JJAINoauIxotRJF+yHxsIuV9yUWcc95COqEOblfsTQAJ3EKj4x8v5gQivMvB3"
							+ "lEmoQcDJBlyYkjuUFoTSEDqqly18Sv1edBykM+ETbIsCAwEAAaNdMFswDgYDVR0P"
							+ "AQH/BAQDAgeAMBIGA1UdEwEB/wQIMAYBAf8CAQAwFgYDVR0lAQH/BAwwCgYIKwYB"
							+ "BQUHAwgwHQYDVR0OBBYEFKw7i135Eoilmr9A1FZROfjcLLzNMA0GCSqGSIb3DQEB"
							+ "BQUAA4GBAL0k0hSvPsv3Nu7CnqM3Q90uAG6fwTkMIhD0LOgYW2pLKMbPn/o+dk2P"
							+ "3OowmXjuNGw4lLwsd8YiZ/3RTXGTkSvGcWjZRDoHm9pe3UK3EdGcaV0g946jJQZt"
							+ "q8akf0kPib8a3ur9y8xmXbMx4/P62qe+qLNV9BwUVgcpZfQJIsCv")), AC_JUS(
			"AC_JUS",
			Base64
					.decode("MIIEODCCAyCgAwIBAgIBFTANBgkqhkiG9w0BAQUFADCBtDELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxETAPBgNVBAcTCEJy"
							+ "YXNpbGlhMQswCQYDVQQIEwJERjExMC8GA1UEAxMoQXV0b3JpZGFkZSBDZXJ0aWZp"
							+ "Y2Fkb3JhIFJhaXogQnJhc2lsZWlyYTAeFw0wNTEyMTkxMzAwMDBaFw0xMTA2MTky"
							+ "MzU5MDBaMFAxCzAJBgNVBAYTAkJSMRMwEQYDVQQKEwpJQ1AtQnJhc2lsMSwwKgYD"
							+ "VQQDEyNBdXRvcmlkYWRlIENlcnRpZmljYWRvcmEgZGEgSnVzdGljYTCCASIwDQYJ"
							+ "KoZIhvcNAQEBBQADggEPADCCAQoCggEBAMZdinSE3Ly323/o9QhmoXxj58rV88m8"
							+ "nNzlBPjs84dSB4nvaYTwCwZtbhk+NEbCzs8ydDL5JveYNqIP1sBtqxVz+nfEdDHV"
							+ "WdwJuPwrX+q5+zkv91C/nUDfvp6ZcKh2ufGeAdI6YjDiqiPJL8wUxOd4W/KAbejp"
							+ "6yynu6X3uUFaK4ziBEsTV5WwSnoh569KSJoDGr1Gyeo/2Di48UdAT3eEemW8l56G"
							+ "nwnXX+ZwDKwPMp4rn4nq9n+7VUxlProo+xEe3OUNe8iVXftRLjTp5nrvkXMwJ/g8"
							+ "Pu3CpsQUJgDl/GRpuUCB0Tb4c9zVVqu+DTToEytyV47VxJwLOFS76esCAwD3v6OB"
							+ "tzCBtDA9BgNVHR8ENjA0MDKgMKAuhixodHRwOi8vYWNyYWl6LmljcGJyYXNpbC5n"
							+ "b3YuYnIvTENSYWNyYWl6LmNybDASBgNVHSAECzAJMAcGBWBMAQETMB0GA1UdDgQW"
							+ "BBS+OdrawXcl4K69Gs6kW5/AemD8ZTAfBgNVHSMEGDAWgBSK+vFXhBETNZBC+ldJ"
							+ "VGkNpMTwNzAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG"
							+ "9w0BAQUFAAOCAQEAu22EJMIck6Q5E/piA3I26+JjChyuwU4UOWdk1GxqJ2AiI11N"
							+ "ZrjoEHc8W0gFTKZFFaywwBZYVqHvWao7iyRCDCXi4q2iVnPdA3GpMpuM69E2a5sf"
							+ "lH5Xd4VUoLpbozdio6hqjRSChX47MiIGu+B2b0DvtejdIIhkElnI2yysa0FhP7af"
							+ "qqr/FjH4Z1nDFxfJPIPQ3+u+nVICIGF7Fx/RCefIxo7+1/7e4IPsOdgCaJnsAE3x"
							+ "Kr1tz8mC+Wd8WR8ieeWwcEDt7frV1vXHSeqA8n0QwaNWfYneDWqklcr7Z9Z6bu6B"
							+ "yQfHRF6V/bSFpw6nZkYHZs7JO3w+3wmyJvc7Tg==")), AC_CAIXA_JUS(
			"AC_CAIXA_JUS",
			Base64
					.decode("MIIFlTCCBH2gAwIBAgISMjAwNjA0MDcxNTM2NDYwMDAxMA0GCSqGSIb3DQEBBQUA"
							+ "MFAxCzAJBgNVBAYTAkJSMRMwEQYDVQQKEwpJQ1AtQnJhc2lsMSwwKgYDVQQDEyNB"
							+ "dXRvcmlkYWRlIENlcnRpZmljYWRvcmEgZGEgSnVzdGljYTAeFw0wNjA0MDcxNTUz"
							+ "MzZaFw0xMTA1MzExNTUzMzZaMHAxCzAJBgNVBAYTAkJSMRMwEQYDVQQKEwpJQ1At"
							+ "QnJhc2lsMTUwMwYDVQQLEyxBdXRvcmlkYWRlIENlcnRpZmljYWRvcmEgZGEgSnVz"
							+ "dGljYSAtIEFDLUpVUzEVMBMGA1UEAxMMQUMgQ0FJWEEtSlVTMIIBIjANBgkqhkiG"
							+ "9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxPeks/OhVO9rsPjWGLmx0wb0B5MfDsOsoEOs"
							+ "nO/Yh6jAiJ0Br+fUCAIWq0ehN4AWMuiemNvGdJRqJW2I42ME5WPfgPgYNMLQAopo"
							+ "oipRo9S8C0BJm1ci0ATXbDvp68lsBHFVlo1ZmyQttnz7Vb1Mlruq6S5u6seezp2z"
							+ "STFjVwzp9XVaYnutz1uD1IeCEPIdkgBRTlXP95JjacX6xnG7g2hZ2DvY1E8wqNZq"
							+ "yyQB/qyQf1gQj7z93g51d5C1FUCSVQRarwI3zEQhfi5UrhACi4wZdBsNzk7GlTLc"
							+ "gKLKvxNLyPG804qj8hEeOUuc5FTU1visVIeNnknAGTxIHJ/DGQIDAQABo4ICRzCC"
							+ "AkMwEgYDVR0TAQH/BAgwBgEB/wIBADAfBgNVHSMEGDAWgBS+OdrawXcl4K69Gs6k"
							+ "W5/AemD8ZTAdBgNVHQ4EFgQUDNDX/gMFm70hiQQ/Esavo6rXni4wDgYDVR0PAQH/"
							+ "BAQDAgEGMIIBnwYDVR0gBIIBljCCAZIwQQYGYEwBAgEWMDcwNQYIKwYBBQUHAgEW"
							+ "KWh0dHA6Ly93d3cuYWNqdXMuZ292LmJyL2FjanVzL2RwY2FqdXMucGRmMEEGBmBM"
							+ "AQICBTA3MDUGCCsGAQUFBwIBFilodHRwOi8vd3d3LmFjanVzLmdvdi5ici9hY2p1"
							+ "cy9kcGNhanVzLnBkZjBBBgZgTAECAxMwNzA1BggrBgEFBQcCARYpaHR0cDovL3d3"
							+ "dy5hY2p1cy5nb3YuYnIvYWNqdXMvZHBjYWp1cy5wZGYwQQYGYEwBAmUFMDcwNQYI"
							+ "KwYBBQUHAgEWKWh0dHA6Ly93d3cuYWNqdXMuZ292LmJyL2FjanVzL2RwY2FqdXMu"
							+ "cGRmMEEGBmBMAQJmBDA3MDUGCCsGAQUFBwIBFilodHRwOi8vd3d3LmFjanVzLmdv"
							+ "di5ici9hY2p1cy9kcGNhanVzLnBkZjBBBgZgTAECZwYwNzA1BggrBgEFBQcCARYp"
							+ "aHR0cDovL3d3dy5hY2p1cy5nb3YuYnIvYWNqdXMvZHBjYWp1cy5wZGYwOgYDVR0f"
							+ "BDMwMTAvoC2gK4YpaHR0cDovL3d3dy5hY2p1cy5nb3YuYnIvYWNqdXMvYWNqdXN2"
							+ "MS5jcmwwDQYJKoZIhvcNAQEFBQADggEBAHJhUoT96zSCDM68Gc7u40gotmTFoxN3"
							+ "JFIBOhi7aO2qABGdyRTJUq4+LzwGXk0BljF4G+sMpBZRAuy2a3sfPYFQggnK2iMl"
							+ "acRaLMkxjxhYEAMvUTptatQvsyqJAZHh/a9VKwpv98b6y1N1ZzRxQaof55w/aC5Y"
							+ "rCzal8gUSl6qJOeDF9nbarbN7FT2B5NpE34hOfLctuJG8WKr4nF4eRQR8baxpiBe"
							+ "YaAQAK0TZ14JCLipeAnivAoR+7OsIT9gk6JF+C2fQDkAWd/GX+PPsnSGJvUntoz/"
							+ "CKCkL+YS/e1kh3EqUMEXYmTKZm9lwDpzZSPVdpRieCqQNtcjXm5R2L8=")), AC_CAIXA_JUS_V1(
			"AC_CAIXA_JUS_V1",
			Base64
					.decode("MIIE7jCCA9agAwIBAgIBBTANBgkqhkiG9w0BAQUFADCBkjELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxLzAtBgNVBAMTJkF1"
							+ "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBkYSBKdXN0aWNhIHYzMB4XDTExMDExNDEy"
							+ "NTkzNFoXDTE5MDExNDEyNTkzNFowczELMAkGA1UEBhMCQlIxEzARBgNVBAoTCklD"
							+ "UC1CcmFzaWwxNTAzBgNVBAsTLEF1dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBkYSBK"
							+ "dXN0aWNhIC0gQUMtSlVTMRgwFgYDVQQDEw9BQyBDQUlYQS1KVVMgdjEwggEiMA0G"
							+ "CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDEYNAcdu1HPO7pMb+RFHxdrpXFvaLO"
							+ "p9cwak3Ne8gpG50gc711JuLL4RInT3lp7TZbtZwWM1n1g9aahxkGeOxLlDIQaTTd"
							+ "gTFP+SYljVU0w9LR37GDJ7TzNlVHGx5pskNyVYU4ENwX8nJHTN/d9XObJyVojUDd"
							+ "rqjYLRismXkA7UlJ35HlWySTRzv6aCCu3OATlasD2fbKbBpYeNPJXraoj/mMrsLQ"
							+ "J51QyR4z4fqsr5K57MuBxLTE1HdAQzPfiw43FFJrMLogK397vlSpHCDLDpBOWfBU"
							+ "1EsOvZwdso+T3fXG15XK2b1KiMH3DtvXy5TtkUJ/1U7ttpFA3+2rbuxPAgMBAAGj"
							+ "ggFrMIIBZzCBkwYDVR0gBIGLMIGIMEIGBmBMAQIBFjA4MDYGCCsGAQUFBwIBFipo"
							+ "dHRwOi8vd3d3LmFjanVzLmp1cy5ici9hY2p1cy9kcGNhY2p1cy5wZGYwQgYGYEwB"
							+ "AgMTMDgwNgYIKwYBBQUHAgEWKmh0dHA6Ly93d3cuYWNqdXMuanVzLmJyL2FjanVz"
							+ "L2RwY2FjanVzLnBkZjBrBgNVHR8EZDBiMC+gLaArhilodHRwOi8vd3d3LmFjanVz"
							+ "Lmp1cy5ici9hY2p1cy9hY2p1c3YzLmNybDAvoC2gK4YpaHR0cDovL2xjci5hY2p1"
							+ "cy5qdXMuYnIvYWNqdXMvYWNqdXN2My5jcmwwHwYDVR0jBBgwFoAUP02gr3F4RFoe"
							+ "vgZsmus1wxley+MwHQYDVR0OBBYEFKnX6bXDwERDwyNv7b7oWNWQTsK0MBIGA1Ud"
							+ "EwEB/wQIMAYBAf8CAQAwDgYDVR0PAQH/BAQDAgEGMA0GCSqGSIb3DQEBBQUAA4IB"
							+ "AQC2Wx0D2I1MFMRyIFAe86ba+3pptFhY9qOmt0IleYdD88798jcXANki+kINnqMb"
							+ "xdkbQJvqEaeO3YvNrbkWQ2thv1RgHP3WkbFW8gPSGkTr/DkcrV1qr2o/J5NJVO99"
							+ "Mt7pecQhjYUDo1BXGPpsKnhPBYoSRcNtrrNFgpJQwzfgD+hpBOhSqHMRiQAHSFg8"
							+ "lL7OCCSIlH93gpyyDbSvl175FOm794LUo/oL9S58rXbmtrvtzWlrABh5RlXpjxGa"
							+ "TVHbmhD8ABXaiHDKbWOTYu652qymaXs1+bmfsMmHe+wUASMhP19tI7wmY8OdMvb2"
							+ "OCBPKf5ItbldH/TvrgZIuxvX")), AC_JUS_V3(
			"AC_JUS_V3",
			Base64
					.decode("MIIEYjCCA0qgAwIBAgIBCDANBgkqhkiG9w0BAQUFADCBlzELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1"
							+ "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjEwHhcNMDkw"
							+ "NjEyMTkyMDMzWhcNMTkwNjEyMTkyMDMzWjCBkjELMAkGA1UEBhMCQlIxEzARBgNV"
							+ "BAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25hbCBkZSBU"
							+ "ZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxLzAtBgNVBAMTJkF1dG9yaWRh"
							+ "ZGUgQ2VydGlmaWNhZG9yYSBkYSBKdXN0aWNhIHYzMIIBIjANBgkqhkiG9w0BAQEF"
							+ "AAOCAQ8AMIIBCgKCAQEAwfnKu1AlD1Y56afLxPDzv5Njus9hYhb87n/Uyjgmh14N"
							+ "tuhmw7EollEDlzceWOHq7+YdxRoBOZdZ21uFIaPxLbdszOGuWmFG5djULFqLBapv"
							+ "7oGs4gzhzFyzvdxBy+3taK3sOuUJxL3A2Ig37rHF75n1YQeS3pX6M0v9FVfuuiCS"
							+ "7rwK1px3e0g+RGqGeH3XidtQem4ysNEEnPiV3b6cBPWA3XIvOuvHaYqIT/g4T4Dw"
							+ "ut8ftgfB8QVa9JEsaxMDLLZ3WfygGo8nvXKgYP/5pVksSl0YQlnOH/mx1TK4ADMS"
							+ "QV8ZWEqQZ6AY5DoiouBdmLje/pTKhzfGBdQOHaZvjwIDAQABo4G7MIG4MA8GA1Ud"
							+ "EwEB/wQFMAMBAf8wDgYDVR0PAQH/BAQDAgEGMBQGA1UdIAQNMAswCQYFYEwBARMw"
							+ "ADA/BgNVHR8EODA2MDSgMqAwhi5odHRwOi8vYWNyYWl6LmljcGJyYXNpbC5nb3Yu"
							+ "YnIvTENSYWNyYWl6djEuY3JsMB8GA1UdIwQYMBaAFEKyLFx0AQe+m/9VMzvuKbtd"
							+ "kb8GMB0GA1UdDgQWBBQ/TaCvcXhEWh6+Bmya6zXDGV7L4zANBgkqhkiG9w0BAQUF"
							+ "AAOCAQEAWepLGyigHBzfHNj5g3N37ac+wLFF4hAC6G1Uji7YTwvZEknnc/Nn2CyW"
							+ "w9LgBnFCSRdpztsuPaqMFKjNjUx80BsFGn38DUhqD3T6t0mv+3XCnFzyxnGj/TXG"
							+ "18MwMhi7z/w4FOiFrh2n2qgmomkL1hrzdYn5X/BmVktSnqcSOA/eZqPJpkAStMgb"
							+ "+SR5uOroSC2Fo7K+UpTsJYbEwRAQAcU3HgDlvd+w20NPI7AeD66LgDIUduGJTxWI"
							+ "qC+4J2wM3B0BFVGIjGHR6IqfHSS+NVJLhf5FEHQH/x0OrdOkEcNjHGPXeiRIn+f3"
							+ "hunSu/nP0gp3WIxbThbGeDW4SgruJw==")), AC_CERTISIGN_JUS_G2(
			"AC_CERTISIGN_JUS_G2",
			Base64
					.decode("MIIGjTCCBXWgAwIBAgIBBDANBgkqhkiG9w0BAQUFADCBkjELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxLzAtBgNVBAMTJkF1"
							+ "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBkYSBKdXN0aWNhIHYzMB4XDTA5MDYxOTE1"
							+ "MjUwMVoXDTE3MDYxOTE1MjUwMVowdzELMAkGA1UEBhMCQlIxEzARBgNVBAoTCklD"
							+ "UC1CcmFzaWwxNTAzBgNVBAsTLEF1dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBkYSBK"
							+ "dXN0aWNhIC0gQUMtSlVTMRwwGgYDVQQDExNBQyBDZXJ0aXNpZ24tSlVTIEcyMIIB"
							+ "IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1fVOFmCrWbgadqfLRaTuxDdH"
							+ "MjSoSsT6fEnhZB4EqgQDpnh+YtAe54fbNrDdqU7gS/33+GmFEMAMU54REZbsoE7H"
							+ "qfaKd6ITJvncy8ME6t2mBS5J9JTyeF1ciD6N5PCBPomY3EoNE9JlUhptSRJNiqI/"
							+ "cQZrP9A71ajGxQBx2TAsMIjNwFotCk33JPeFHpLniEL1P18X9SXBQjclL1hOhzcp"
							+ "N8kayc7sUR0DTIZQRJFFa7dBdeKPL8t1NFwzGsClqcrXb36vbdboWKmY7mwJl0yw"
							+ "gJ5RZj1ihKaMhF03PAUlBgVokwDRvlUVyXWzbLQOii1gcX2rFxLPSUYW3qucXwID"
							+ "AQABo4IDBjCCAwIwggItBgNVHSAEggIkMIICIDBCBgZgTAECARgwODA2BggrBgEF"
							+ "BQcCARYqaHR0cDovL3d3dy5hY2p1cy5qdXMuYnIvYWNqdXMvZHBjYWNqdXMucGRm"
							+ "MEIGBmBMAQICBjA4MDYGCCsGAQUFBwIBFipodHRwOi8vd3d3LmFjanVzLmp1cy5i"
							+ "ci9hY2p1cy9kcGNhY2p1cy5wZGYwQgYGYEwBAgMVMDgwNgYIKwYBBQUHAgEWKmh0"
							+ "dHA6Ly93d3cuYWNqdXMuanVzLmJyL2FjanVzL2RwY2FjanVzLnBkZjBCBgZgTAEC"
							+ "BAgwODA2BggrBgEFBQcCARYqaHR0cDovL3d3dy5hY2p1cy5qdXMuYnIvYWNqdXMv"
							+ "ZHBjYWNqdXMucGRmMEIGBmBMAQJlBjA4MDYGCCsGAQUFBwIBFipodHRwOi8vd3d3"
							+ "LmFjanVzLmp1cy5ici9hY2p1cy9kcGNhY2p1cy5wZGYwQgYGYEwBAmYFMDgwNgYI"
							+ "KwYBBQUHAgEWKmh0dHA6Ly93d3cuYWNqdXMuanVzLmJyL2FjanVzL2RwY2FjanVz"
							+ "LnBkZjBCBgZgTAECZwcwODA2BggrBgEFBQcCARYqaHR0cDovL3d3dy5hY2p1cy5q"
							+ "dXMuYnIvYWNqdXMvZHBjYWNqdXMucGRmMEIGBmBMAQJoBTA4MDYGCCsGAQUFBwIB"
							+ "FipodHRwOi8vd3d3LmFjanVzLmp1cy5ici9hY2p1cy9kcGNhY2p1cy5wZGYwawYD"
							+ "VR0fBGQwYjAvoC2gK4YpaHR0cDovL3d3dy5hY2p1cy5qdXMuYnIvYWNqdXMvYWNq"
							+ "dXN2My5jcmwwL6AtoCuGKWh0dHA6Ly9sY3IuYWNqdXMuanVzLmJyL2FjanVzL2Fj"
							+ "anVzdjMuY3JsMB8GA1UdIwQYMBaAFD9NoK9xeERaHr4GbJrrNcMZXsvjMB0GA1Ud"
							+ "DgQWBBSPlvhiDOXJae+DUhMYTfbC8Nt4DjASBgNVHRMBAf8ECDAGAQH/AgEAMA4G"
							+ "A1UdDwEB/wQEAwIBBjANBgkqhkiG9w0BAQUFAAOCAQEAfJWnJzdCUTXw+TPZN9qF"
							+ "x0Aj/TH30yWmY+RrK+5HdYaAxXVOJls/r8du9qiY+KJ9O13AgsnNrF335E36QGkB"
							+ "sVjBuavCMHI+Sa0h9nKYHkmBe7mggDiWFF1Roc99JMEGxtuFxp7HMuVnGjU8SKBE"
							+ "WwVKToCwRNUDrhTc0ZNRumrs4pr3UVcaZUWoP+eT5uKH4iUMfYg9ZU0tFBxmp57J"
							+ "TJYZ3yCaHbndOMn8yBS/LcyphEiVJ65BeLgkve2ZiLImXERE5pDm6dDbddEsDtIK"
							+ "+0rg/BCw41gH4blEK4xl7QlWegWPca0r2UM5q+7ovxW/GFj3udVWZygEeqbSAURU"
							+ "ug==")), AC_RFB_V2(
			"AC_RFB_V2",
			Base64
					.decode("MIIEEDCCAvigAwIBAgIBBTANBgkqhkiG9w0BAQUFADCBlzELMAkGA1UEBhMCQlIx"
							+ "EzARBgNVBAoTCklDUC1CcmFzaWwxPTA7BgNVBAsTNEluc3RpdHV0byBOYWNpb25h"
							+ "bCBkZSBUZWNub2xvZ2lhIGRhIEluZm9ybWFjYW8gLSBJVEkxNDAyBgNVBAMTK0F1"
							+ "dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBSYWl6IEJyYXNpbGVpcmEgdjEwHhcNMDgx"
							+ "MDI0MTIzOTQ2WhcNMTgxMDI0MTIzOTQ2WjBXMQswCQYDVQQGEwJCUjETMBEGA1UE"
							+ "ChMKSUNQLUJyYXNpbDEzMDEGA1UEAxMqQUMgU2VjcmV0YXJpYSBkYSBSZWNlaXRh"
							+ "IEZlZGVyYWwgZG8gQnJhc2lsMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC"
							+ "AQEA3o1phpOoNH/BA5ydDgeDndk7mItSVUK7t3PNjCkF/7lCSM3SiQxi/XsnvS3f"
							+ "4NCR5Vq/4yGrWa31FJi/XYRlLZH46L86S2hOBkV5sIxrr0EPN5q/EUTQCrcbcqgr"
							+ "V9QX2SSPqOO5+wS8+zygNfBAOPFSLpqwyzSYS+ZQdeDkuhFMikF07e7XwfRvc+/K"
							+ "eR0RU1p8GijtAtqsXGmq/8wunlySd7g+h2CfkILnY87GyB3UwOP7zSOaV2Un9/yz"
							+ "dBRduGpUZSx2yn8FrBB7mCfE58gJo5YZ1KZfcSfRNrAaQW6B7mDLsRnDdn5jYQnn"
							+ "0w/xf9Kwq3YFB1rZKRDBGLqhVwIDAQABo4GlMIGiMA8GA1UdEwEB/wQFMAMBAf8w"
							+ "DgYDVR0PAQH/BAQDAgEGMD8GA1UdHwQ4MDYwNKAyoDCGLmh0dHA6Ly9hY3JhaXou"
							+ "aWNwYnJhc2lsLmdvdi5ici9MQ1JhY3JhaXp2MS5jcmwwHwYDVR0jBBgwFoAUQrIs"
							+ "XHQBB76b/1UzO+4pu12RvwYwHQYDVR0OBBYEFBtdm3eNVCVtBaPCcqy/d/mxPDhL"
							+ "MA0GCSqGSIb3DQEBBQUAA4IBAQAfE0z7q9dicmpuK8TbkmFjb5NdR4xxy5n2H+hI"
							+ "S/mxAXXUxD1MHQltmFzjGjCVLKxfnLXDFPzfEuB1KCEACseAS1q2iAgDaEOAbVoG"
							+ "iSNZF5Tx8wCdkGXszBC8kdM4RUsLqVAAIkNxH+6K7I9r9Z5/8oNg/JZHCnNP5BLq"
							+ "1nKxplltISTTfNecUP2bY/7f24oGlYfoYwtzPZEDu1Zw01jHbT5K4vaMG4pV81d6"
							+ "exb+paeCVcZItAo8lxc35ago9RG5HyWO0Ep08CVzesO5CNVhFb5BQDKO/fDDyNPH"
							+ "DirDKnx6ZJAclztk6KtIJK9yjixYkBeG6J78eYMhnxJJmfRh")), AC_CERTISIGN_RFB_G3(
			"AC_CERTISIGN_RFB_G3",
			Base64
					.decode("MIIE8DCCA9igAwIBAgIBAzANBgkqhkiG9w0BAQUFADBXMQswCQYDVQQGEwJCUjET"
							+ "MBEGA1UEChMKSUNQLUJyYXNpbDEzMDEGA1UEAxMqQUMgU2VjcmV0YXJpYSBkYSBS"
							+ "ZWNlaXRhIEZlZGVyYWwgZG8gQnJhc2lsMB4XDTA4MTAyOTE4MzM1M1oXDTE2MTAy"
							+ "OTE4MzM1M1oweDELMAkGA1UEBhMCQlIxEzARBgNVBAoTCklDUC1CcmFzaWwxNjA0"
							+ "BgNVBAsTLVNlY3JldGFyaWEgZGEgUmVjZWl0YSBGZWRlcmFsIGRvIEJyYXNpbCAt"
							+ "IFJGQjEcMBoGA1UEAxMTQUMgQ2VydGlzaWduIFJGQiBHMzCCASIwDQYJKoZIhvcN"
							+ "AQEBBQADggEPADCCAQoCggEBAKbdyy+BUmEaNq8A7PyWX8C6xRGkdn8K8TRgAPUm"
							+ "ncAUEpLZhqdu5l66Tf6cEdkiMxityVIAIUKQPnc2fqUqhHgLJXb4ALQlCx0T3VgV"
							+ "9QhTO/dDr7zH+Kbi3r1Wq/alJPcnm3mb8ttINpJ+FaAaSK9MkAMwCFopcNsxfH+m"
							+ "Jp3AcUjK68RzUjsOaIbZ5h6CjYF2EJMtQXEJ8Da97IFCjOsafRbbojtL5vkO1Hdo"
							+ "zbqIN2vGVeqeCC3maycjTsNxIHmELtVUQDG1xwK1jce+Ghjs16YR7V3PT/x6ENzo"
							+ "8MktDheYl4/0U/xvnBS9TsERqW6Z4hY8TAeSLkogc9H3IkcCAwEAAaOCAaQwggGg"
							+ "MIH1BgNVHSAEge0wgeowTAYGYEwBAgEMMEIwQAYIKwYBBQUHAgEWNGh0dHA6Ly93"
							+ "d3cucmVjZWl0YS5mYXplbmRhLmdvdi5ici9hY3JmYi9kcGNhY3JmYi5wZGYwTAYG"
							+ "YEwBAgMGMEIwQAYIKwYBBQUHAgEWNGh0dHA6Ly93d3cucmVjZWl0YS5mYXplbmRh"
							+ "Lmdvdi5ici9hY3JmYi9kcGNhY3JmYi5wZGYwTAYGYEwBAgQEMEIwQAYIKwYBBQUH"
							+ "AgEWNGh0dHA6Ly93d3cucmVjZWl0YS5mYXplbmRhLmdvdi5ici9hY3JmYi9kcGNh"
							+ "Y3JmYi5wZGYwQgYDVR0fBDswOTA3oDWgM4YxaHR0cDovL3d3dy5yZWNlaXRhLmZh"
							+ "emVuZGEuZ292LmJyL2FjcmZiL2FjcmZiLmNybDAfBgNVHSMEGDAWgBQbXZt3jVQl"
							+ "bQWjwnKsv3f5sTw4SzAdBgNVHQ4EFgQU/IBr1U3R/HjYbGQvYUs4p4Lw3J0wEgYD"
							+ "VR0TAQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMCAQYwDQYJKoZIhvcNAQEFBQAD"
							+ "ggEBANgf9Wcg8eKwMFznk2ibOjhp5PGip0/c5bcBe2WYPi/h7Bi5NsHXYyoQQV7b"
							+ "KHS5yrqEg7uvsid5X8ZhHN/aIeiaiUP/bZnQidtuRM2dZOwfUr8dW1agOdfxwwcz"
							+ "BaOx6CNo/TEysDiuINb46c+xzbFI8xDPtONXWlelx0s1wiUt2RJNHusby6XBGbHM"
							+ "Oh4BRIVe+C/qC/pY9gj1c5pzG+Sa18T9XxEfsWEZRXcDHwrlI3+61ZLwe7fM7OcA"
							+ "LXHZCfefx/Xqs8wkb46uNetJuASVkFgn6/OlesJ9yeyHRuKz3LHNRyKbPHGfAA5y"
							+ "lwHLCVPhSeF1oJ8Y9YrPlo8nVBs="))

	;

	private final String nome;
	private final byte[] certificado;

	CertificadoACEnum(String nome, byte[] certificado) {
		this.nome = nome;
		this.certificado = certificado;
	}

	public String getNome() {
		return nome;
	}

	public byte[] getCertificado() {
		return this.certificado;
	}

	public String toString() {
		return nome;
	}

	public boolean seHomonimaA(CertificadoACEnum outra) {
		if (outra == null)
			return false;
		return outra.getNome().equals(this.getNome());
	}

	public boolean seTemCertificadoIdenticoA(CertificadoACEnum outra) {
		if (outra == null)
			return false;
		if (outra.getCertificado().length != this.getCertificado().length)
			return false;
		for (int i = 0; i < outra.getCertificado().length; i++) {
			if (outra.getCertificado()[i] != this.getCertificado()[i])
				return false;
		}
		return true;
	}

	/**
	 * obtém o certificado como TrustAnchor (Certificado auto assinado
	 * confiável)
	 * 
	 * @return
	 */
	public TrustAnchor toTrustAnchor() {
		try {
			return new TrustAnchor((X509Certificate) (CertificateFactory
					.getInstance("X.509"))
					.generateCertificate(new ByteArrayInputStream(this
							.getCertificado())), null);
		} catch (CertificateException e) {
			return null;
		}

	}

	/**
	 * obtém o certificado como X509Certificate
	 * 
	 * @return
	 */
	public X509Certificate toX509Certificate() {
		try {
			return (X509Certificate) (CertificateFactory.getInstance("X.509"))
					.generateCertificate(new ByteArrayInputStream(this
							.getCertificado()));
		} catch (CertificateException e) {
			return null;
		}
	}
}
