package br.gov.siga.libs.webwork;

import org.junit.Test;
import static org.junit.Assert.*;

import br.gov.jfrj.siga.libs.webwork.SigaAnonimoActionSupport;

public class SigaAnonimoActionSupportTest {
	
	@Test
	public void paramIntegerDeveRetornarNullSeParamRetornarNull() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return null;
			}
		};
		Integer paramInteger = sa.paramInteger("");
		assertNull( paramInteger );
	}
	
	@Test
	public void paramIntegerDeveRetornarNullSeParamRetornarStringVazia() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "";
			}
		};
		Integer paramInteger = sa.paramInteger("");
		assertNull( paramInteger );
	}
	
	@Test
	public void paramIntegerDeveRetornarNullSeParamRetornarStringNaoNumerica() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "pes 28";
			}
		};
		Integer paramInteger = sa.paramInteger("");
		assertNull( paramInteger );
	}
	
	@Test
	public void paramIntegerDeveRetornarIntegerSeParamRetornarStringNumerica() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "28";
			}
		};
		Integer paramInteger = sa.paramInteger("");
		assertEquals( 28 ,  paramInteger.intValue() );
	}
	
	@Test
	public void paramLongDeveRetornarNullSeParamRetornarNull() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return null;
			}
		};
		Long paramLong = sa.paramLong("");
		assertNull( paramLong );
	}
	
	@Test
	public void paramLongDeveRetornarNullSeParamRetornarStringVazia() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "";
			}
		};
		Long paramLong = sa.paramLong("");
		assertNull( paramLong );
	}
	
	@Test
	public void paramLongDeveRetornarNullSeParamRetornarStringNaoNumerica() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "pes 28";
			}
		};
		Long paramLong = sa.paramLong("");
		assertNull( paramLong );
	}
	
	@Test
	public void paramLongDeveRetornarLongSeParamRetornarStringNumerica() throws Exception {
		SigaAnonimoActionSupport sa = new SigaAnonimoActionSupport(){
			private static final long serialVersionUID = 1L;
			@Override
			public String param(String parameterName) {
				return "28";
			}
		};
		Long paramLong = sa.paramLong("");
		assertEquals( 28, paramLong.longValue() );
	}

}
