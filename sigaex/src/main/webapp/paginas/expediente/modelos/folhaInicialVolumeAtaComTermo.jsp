<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
 <table  cellpadding="0" cellspacing="0" width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		     <tr>
		        <td  valign="top" align="center" width="50%" style="border-collapse: collapse; border-color: black;" >
		           <table width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		              <tr bgcolor="#C0C0C0"">
		                 <td  width="38%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Nota de Empenho<br/>&nbsp</td>
		                 <td  width="17%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Tipo<br/>&nbsp</td>
		                 <td  width="17%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Data<br/>&nbsp</td>
		                 <td  width="17%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Valor <br/>(R$)&nbsp</td>
		                 <td  width="11%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="6">
		                 <tr >           
		                    <td height="26px"  width="38%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                    <td  width="17%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                    <td  width="17%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                    <td  width="17%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                    <td  width="11%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table>
		           <table  width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		              <tr  bgcolor="#C0C0C0">
		                 <td  width="20%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp</td>
		                 <td height="26px" width="19%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Vigência</td>
		                 <td  width="12%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		                 <td  width="13%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Public.<br/>Fls.</td>
		                 <td  width="18%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">SIAFI</td>
		                 <td  width="18%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">SIASG</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Ata Nº</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Contrato Nº</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Garantia</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Contrato Nº</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Garantia</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td height="26px"    align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Contrato Nº</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Garantia</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		               <tr >
		                 <td height="26px"    align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Contrato Nº</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <tr >
		                 <td  height="26px"   align="center" height="26px" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Garantia</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td    align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		           </table>
		        </td>
		         <td valign="top" align="center" width="50%" style="border-collapse: collapse; border-color: black;" >
		           <table  border="1"  width="100%" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		              <tr  bgcolor="#C0C0C0">
		                 <td  width="22%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">CND<br/>Válida até</td>
		                 <td  width="12%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		                 <td  width="26%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">CCN<br/>Válida até</td>
		                 <td  width="12%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		                 <td  width="18%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">CRF<br/>Válida até</td>
		                 <td  width="12%" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="6">
		                 <tr >           
		                    <td  height="26px" width="0%" height="26px" align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  width="0%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  width="0%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  width="0%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  width="0%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  width="0%"  align="center" style="font-size: 7pt; border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 </tr>
		              </c:forEach>   
		              </table>
		              <table  border="1"  width="100%" style="border-color: black; border-spacing: 0px; border-collapse: collapse"  width="100%">
		              <tr >
		                 <td  width="22%" height="26px" bgcolor="#C0C0C0" align="center" style="border-collapse: collapse; border-color: black; font-size: 7pt">&nbsp;</td>
		                 <td  width="12%" bgcolor="#C0C0C0" align="center" style="border-collapse: collapse; border-color: black;font-size: 7pt">&nbsp;</td>
		                 <td  width="26%" bgcolor="#C0C0C0" align="center" style="border-collapse: collapse; border-color: black;font-size: 7pt">&nbsp;</td>
		                 <td  width="12%" bgcolor="#C0C0C0" align="center" style="border-collapse: collapse; border-color: black;font-size: 7pt">&nbsp;</td>
		                 <td  width="18%"  align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 <td  width="12%"  align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="8">
		                 <tr >           
		                    <td height="26px" align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td  align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td   align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td   align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td   align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                    <td   align="center" style="border-collapse: collapse; border-color: black;">&nbsp;</td>
		                 </tr>
		              </c:forEach>
		              </table>   
		 
		           </table>
		        </td>
		     </tr>
		  </table>	