-------------------------------------------
--	SCRIPT:CRIACAO DO BANCO
-------------------------------------------

--SET DEFINE OFF;

ALTER SESSION SET CURRENT_SCHEMA=sigawf;

CREATE SEQUENCE  "SIGAWF"."WF_CONHECIMENTO_SEQ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

  CREATE TABLE "SIGAWF"."WF_CONFIGURACAO" 
   (	"ID_CONFIGURACAO_WF" NUMBER(19,0), 
	"NM_PROCEDIMENTO" VARCHAR2(255 CHAR), 
	"NM_RAIA" VARCHAR2(255 CHAR), 
	"NM_TAREFA" VARCHAR2(255 CHAR), 
	"NM_EXPRESSAO" VARCHAR2(255 CHAR), 
	"ID_LOTA_ATOR" NUMBER(19,0), 
	"ID_ATOR" NUMBER(19,0)
   ) ;
--------------------------------------------------------
--  DDL for Table WF_CONHECIMENTO
--------------------------------------------------------

  CREATE TABLE "SIGAWF"."WF_CONHECIMENTO" 
   (	"ID_CONHECIMENTO" NUMBER, 
	"NM_PROCEDIMENTO" VARCHAR2(255), 
	"NM_TAREFA" VARCHAR2(255), 
	"DESC_CONHECIMENTO" VARCHAR2(2000), 
	"HIS_ID_INI" NUMBER, 
	"HIS_DT_INI" DATE, 
	"HIS_IDC_INI" NUMBER, 
	"HIS_DT_FIM" DATE, 
	"HIS_IDC_FIM" NUMBER, 
	"HIS_ATIVO" NUMBER(1,0)
   ) ;
   
   
   --------------------------------------------------------
--  DDL for Function REMOVE_ACENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGAWF"."REMOVE_ACENTO" 
    (acentuado in
    VARCHAR2)

--	Enter the parameters for the function in
--	the brackets above.  If this function has
--	no parameters then delete the line

--  ***************************************************
--	*                                                 *
--	*   Author                                        *
--	*   Creation Date                                 *
--	*   Comments                                      *
--	*                                                 *
--  ***************************************************

RETURN  VARCHAR2

IS

--	Enter all variables cursors and constants following
--	this line
sem_acento VARCHAR2(4000);

begin

--	Enter the code for the function following
--	this line

      sem_acento := CONVERT(TRANSLATE(UPPER( acentuado ),'ÃÕÑ','AON'),'US7ASCII');

	return	sem_acento;
			
exception

--	Enter the code to handle exception conditions
--	following this line


	when others then
		null;
			
end;
 
/

   ---
   --- JBPM
   ---
   
   
create table JBPM_ACTION (ID_ number(19,0) not null, class char(1 char) not null, NAME_ varchar2(255 char), ISPROPAGATIONALLOWED_ number(1,0), ACTIONEXPRESSION_ varchar2(255 char), ISASYNC_ number(1,0), REFERENCEDACTION_ number(19,0), ACTIONDELEGATION_ number(19,0), EVENT_ number(19,0), PROCESSDEFINITION_ number(19,0), EXPRESSION_ clob, TIMERNAME_ varchar2(255 char), DUEDATE_ varchar2(255 char), REPEAT_ varchar2(255 char), TRANSITIONNAME_ varchar2(255 char), TIMERACTION_ number(19,0), EVENTINDEX_ number(10,0), EXCEPTIONHANDLER_ number(19,0), EXCEPTIONHANDLERINDEX_ number(10,0), primary key (ID_));
create table JBPM_BYTEARRAY (ID_ number(19,0) not null, NAME_ varchar2(255 char), FILEDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_BYTEBLOCK (PROCESSFILE_ number(19,0) not null, BYTES_ raw(1024), INDEX_ number(10,0) not null, primary key (PROCESSFILE_, INDEX_));
create table JBPM_COMMENT (ID_ number(19,0) not null, VERSION_ number(10,0) not null, ACTORID_ varchar2(255 char), TIME_ timestamp, MESSAGE_ clob, TOKEN_ number(19,0), TASKINSTANCE_ number(19,0), TOKENINDEX_ number(10,0), TASKINSTANCEINDEX_ number(10,0), primary key (ID_));
create table JBPM_DECISIONCONDITIONS (DECISION_ number(19,0) not null, TRANSITIONNAME_ varchar2(255 char), EXPRESSION_ varchar2(255 char), INDEX_ number(10,0) not null, primary key (DECISION_, INDEX_));
create table JBPM_DELEGATION (ID_ number(19,0) not null, CLASSNAME_ clob, CONFIGURATION_ clob, CONFIGTYPE_ varchar2(255 char), PROCESSDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_EVENT (ID_ number(19,0) not null, EVENTTYPE_ varchar2(255 char), TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSDEFINITION_ number(19,0), NODE_ number(19,0), TRANSITION_ number(19,0), TASK_ number(19,0), primary key (ID_));
create table JBPM_EXCEPTIONHANDLER (ID_ number(19,0) not null, EXCEPTIONCLASSNAME_ clob, TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSDEFINITION_ number(19,0), GRAPHELEMENTINDEX_ number(10,0), NODE_ number(19,0), TRANSITION_ number(19,0), TASK_ number(19,0), primary key (ID_));
create table JBPM_ID_GROUP (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), TYPE_ varchar2(255 char), PARENT_ number(19,0), primary key (ID_));
create table JBPM_ID_MEMBERSHIP (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), ROLE_ varchar2(255 char), USER_ number(19,0), GROUP_ number(19,0), primary key (ID_));
create table JBPM_ID_PERMISSIONS (ENTITY_ number(19,0) not null, CLASS_ varchar2(255 char), NAME_ varchar2(255 char), ACTION_ varchar2(255 char));
create table JBPM_ID_USER (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), EMAIL_ varchar2(255 char), PASSWORD_ varchar2(255 char), primary key (ID_));
create table JBPM_JOB (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, DUEDATE_ timestamp, PROCESSINSTANCE_ number(19,0), TOKEN_ number(19,0), TASKINSTANCE_ number(19,0), ISSUSPENDED_ number(1,0), ISEXCLUSIVE_ number(1,0), LOCKOWNER_ varchar2(255 char), LOCKTIME_ timestamp, EXCEPTION_ clob, RETRIES_ number(10,0), NAME_ varchar2(255 char), REPEAT_ varchar2(255 char), TRANSITIONNAME_ varchar2(255 char), ACTION_ number(19,0), GRAPHELEMENTTYPE_ varchar2(255 char), GRAPHELEMENT_ number(19,0), NODE_ number(19,0), primary key (ID_));
create table JBPM_LOG (ID_ number(19,0) not null, CLASS_ char(1 char) not null, INDEX_ number(10,0), DATE_ timestamp, TOKEN_ number(19,0), PARENT_ number(19,0), MESSAGE_ clob, EXCEPTION_ clob, ACTION_ number(19,0), NODE_ number(19,0), ENTER_ timestamp, LEAVE_ timestamp, DURATION_ number(19,0), NEWLONGVALUE_ number(19,0), TRANSITION_ number(19,0), CHILD_ number(19,0), SOURCENODE_ number(19,0), DESTINATIONNODE_ number(19,0), VARIABLEINSTANCE_ number(19,0), OLDBYTEARRAY_ number(19,0), NEWBYTEARRAY_ number(19,0), OLDDATEVALUE_ timestamp, NEWDATEVALUE_ timestamp, OLDDOUBLEVALUE_ double precision, NEWDOUBLEVALUE_ double precision, OLDLONGIDCLASS_ varchar2(255 char), OLDLONGIDVALUE_ number(19,0), NEWLONGIDCLASS_ varchar2(255 char), NEWLONGIDVALUE_ number(19,0), OLDSTRINGIDCLASS_ varchar2(255 char), OLDSTRINGIDVALUE_ varchar2(255 char), NEWSTRINGIDCLASS_ varchar2(255 char), NEWSTRINGIDVALUE_ varchar2(255 char), OLDLONGVALUE_ number(19,0), OLDSTRINGVALUE_ clob, NEWSTRINGVALUE_ clob, TASKINSTANCE_ number(19,0), TASKACTORID_ varchar2(255 char), TASKOLDACTORID_ varchar2(255 char), SWIMLANEINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_MODULEDEFINITION (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), PROCESSDEFINITION_ number(19,0), STARTTASK_ number(19,0), primary key (ID_));
create table JBPM_MODULEINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, PROCESSINSTANCE_ number(19,0), TASKMGMTDEFINITION_ number(19,0), NAME_ varchar2(255 char), primary key (ID_));
create table JBPM_NODE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), DESCRIPTION_ clob, PROCESSDEFINITION_ number(19,0), ISASYNC_ number(1,0), ISASYNCEXCL_ number(1,0), ACTION_ number(19,0), SUPERSTATE_ number(19,0), SUBPROCNAME_ varchar2(255 char), SUBPROCESSDEFINITION_ number(19,0), DECISIONEXPRESSION_ varchar2(255 char), DECISIONDELEGATION number(19,0), SCRIPT_ number(19,0), PARENTLOCKMODE_ varchar2(255 char), SIGNAL_ number(10,0), CREATETASKS_ number(1,0), ENDTASKS_ number(1,0), NODECOLLECTIONINDEX_ number(10,0), primary key (ID_));
create table JBPM_POOLEDACTOR (ID_ number(19,0) not null, VERSION_ number(10,0) not null, ACTORID_ varchar2(255 char), SWIMLANEINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_PROCESSDEFINITION (ID_ number(19,0) not null, CLASS_ char(1 char) not null, NAME_ varchar2(255 char), DESCRIPTION_ clob, VERSION_ number(10,0), ISTERMINATIONIMPLICIT_ number(1,0), STARTSTATE_ number(19,0), primary key (ID_));
create table JBPM_PROCESSINSTANCE (ID_ number(19,0) not null, VERSION_ number(10,0) not null, KEY_ varchar2(255 char), START_ timestamp, END_ timestamp, ISSUSPENDED_ number(1,0), PROCESSDEFINITION_ number(19,0), ROOTTOKEN_ number(19,0), SUPERPROCESSTOKEN_ number(19,0), primary key (ID_));
create table JBPM_RUNTIMEACTION (ID_ number(19,0) not null, VERSION_ number(10,0) not null, EVENTTYPE_ varchar2(255 char), TYPE_ char(1 char), GRAPHELEMENT_ number(19,0), PROCESSINSTANCE_ number(19,0), ACTION_ number(19,0), PROCESSINSTANCEINDEX_ number(10,0), primary key (ID_));
create table JBPM_SWIMLANE (ID_ number(19,0) not null, NAME_ varchar2(255 char), ACTORIDEXPRESSION_ varchar2(255 char), POOLEDACTORSEXPRESSION_ varchar2(255 char), ASSIGNMENTDELEGATION_ number(19,0), TASKMGMTDEFINITION_ number(19,0), primary key (ID_));
create table JBPM_SWIMLANEINSTANCE (ID_ number(19,0) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), ACTORID_ varchar2(255 char), SWIMLANE_ number(19,0), TASKMGMTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TASK (ID_ number(19,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ clob, PROCESSDEFINITION_ number(19,0), ISBLOCKING_ number(1,0), ISSIGNALLING_ number(1,0), CONDITION_ varchar2(255 char), DUEDATE_ varchar2(255 char), PRIORITY_ number(10,0), ACTORIDEXPRESSION_ varchar2(255 char), POOLEDACTORSEXPRESSION_ varchar2(255 char), TASKMGMTDEFINITION_ number(19,0), TASKNODE_ number(19,0), STARTSTATE_ number(19,0), ASSIGNMENTDELEGATION_ number(19,0), SWIMLANE_ number(19,0), TASKCONTROLLER_ number(19,0), primary key (ID_));
create table JBPM_TASKACTORPOOL (TASKINSTANCE_ number(19,0) not null, POOLEDACTOR_ number(19,0) not null, primary key (TASKINSTANCE_, POOLEDACTOR_));
create table JBPM_TASKCONTROLLER (ID_ number(19,0) not null, TASKCONTROLLERDELEGATION_ number(19,0), primary key (ID_));
create table JBPM_TASKINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ clob, ACTORID_ varchar2(255 char), CREATE_ timestamp, START_ timestamp, END_ timestamp, DUEDATE_ timestamp, PRIORITY_ number(10,0), ISCANCELLED_ number(1,0), ISSUSPENDED_ number(1,0), ISOPEN_ number(1,0), ISSIGNALLING_ number(1,0), ISBLOCKING_ number(1,0), TASK_ number(19,0), TOKEN_ number(19,0), PROCINST_ number(19,0), SWIMLANINSTANCE_ number(19,0), TASKMGMTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TOKEN (ID_ number(19,0) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), START_ timestamp, END_ timestamp, NODEENTER_ timestamp, NEXTLOGINDEX_ number(10,0), ISABLETOREACTIVATEPARENT_ number(1,0), ISTERMINATIONIMPLICIT_ number(1,0), ISSUSPENDED_ number(1,0), LOCK_ varchar2(255 char), NODE_ number(19,0), PROCESSINSTANCE_ number(19,0), PARENT_ number(19,0), SUBPROCESSINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TOKENVARIABLEMAP (ID_ number(19,0) not null, VERSION_ number(10,0) not null, TOKEN_ number(19,0), CONTEXTINSTANCE_ number(19,0), primary key (ID_));
create table JBPM_TRANSITION (ID_ number(19,0) not null, NAME_ varchar2(255 char), DESCRIPTION_ clob, PROCESSDEFINITION_ number(19,0), FROM_ number(19,0), TO_ number(19,0), CONDITION_ varchar2(255 char), FROMINDEX_ number(10,0), primary key (ID_));
create table JBPM_VARIABLEACCESS (ID_ number(19,0) not null, VARIABLENAME_ varchar2(255 char), ACCESS_ varchar2(255 char), MAPPEDNAME_ varchar2(255 char), SCRIPT_ number(19,0), PROCESSSTATE_ number(19,0), TASKCONTROLLER_ number(19,0), INDEX_ number(10,0), primary key (ID_));
create table JBPM_VARIABLEINSTANCE (ID_ number(19,0) not null, CLASS_ char(1 char) not null, VERSION_ number(10,0) not null, NAME_ varchar2(255 char), CONVERTER_ char(1 char), TOKEN_ number(19,0), TOKENVARIABLEMAP_ number(19,0), PROCESSINSTANCE_ number(19,0), BYTEARRAYVALUE_ number(19,0), DATEVALUE_ timestamp, DOUBLEVALUE_ double precision, LONGIDCLASS_ varchar2(255 char), LONGVALUE_ number(19,0), STRINGIDCLASS_ varchar2(255 char), STRINGVALUE_ varchar2(255 char), TASKINSTANCE_ number(19,0), primary key (ID_));
create index IDX_ACTION_EVENT on JBPM_ACTION (EVENT_);
create index IDX_ACTION_ACTNDL on JBPM_ACTION (ACTIONDELEGATION_);
create index IDX_ACTION_PROCDF on JBPM_ACTION (PROCESSDEFINITION_);
create index IDX_COMMENT_TOKEN on JBPM_COMMENT (TOKEN_);
create index IDX_COMMENT_TSK on JBPM_COMMENT (TASKINSTANCE_);
create index IDX_DELEG_PRCD on JBPM_DELEGATION (PROCESSDEFINITION_);
create index IDX_JOB_TSKINST on JBPM_JOB (TASKINSTANCE_);
create index IDX_JOB_PRINST on JBPM_JOB (PROCESSINSTANCE_);
create index IDX_JOB_TOKEN on JBPM_JOB (TOKEN_);
create index IDX_MODDEF_PROCDF on JBPM_MODULEDEFINITION (PROCESSDEFINITION_);
create index IDX_MODINST_PRINST on JBPM_MODULEINSTANCE (PROCESSINSTANCE_);
create index IDX_PSTATE_SBPRCDEF on JBPM_NODE (SUBPROCESSDEFINITION_);
create index IDX_NODE_SUPRSTATE on JBPM_NODE (SUPERSTATE_);
create index IDX_NODE_PROCDEF on JBPM_NODE (PROCESSDEFINITION_);
create index IDX_NODE_ACTION on JBPM_NODE (ACTION_);
create index IDX_PLDACTR_ACTID on JBPM_POOLEDACTOR (ACTORID_);
create index IDX_TSKINST_SWLANE on JBPM_POOLEDACTOR (SWIMLANEINSTANCE_);
create index IDX_PROCDEF_STRTST on JBPM_PROCESSDEFINITION (STARTSTATE_);
create index IDX_PROCIN_ROOTTK on JBPM_PROCESSINSTANCE (ROOTTOKEN_);
create index IDX_PROCIN_SPROCTK on JBPM_PROCESSINSTANCE (SUPERPROCESSTOKEN_);
create index IDX_PROCIN_KEY on JBPM_PROCESSINSTANCE (KEY_);
create index IDX_PROCIN_PROCDEF on JBPM_PROCESSINSTANCE (PROCESSDEFINITION_);
create index IDX_RTACTN_PRCINST on JBPM_RUNTIMEACTION (PROCESSINSTANCE_);
create index IDX_RTACTN_ACTION on JBPM_RUNTIMEACTION (ACTION_);
create index IDX_SWIMLINST_SL on JBPM_SWIMLANEINSTANCE (SWIMLANE_);
create index IDX_TASK_TSKNODE on JBPM_TASK (TASKNODE_);
create index IDX_TASK_PROCDEF on JBPM_TASK (PROCESSDEFINITION_);
create index IDX_TASK_TASKMGTDF on JBPM_TASK (TASKMGMTDEFINITION_);
create index IDX_TASKINST_TOKN on JBPM_TASKINSTANCE (TOKEN_);
create index IDX_TASKINST_TSK on JBPM_TASKINSTANCE (TASK_, PROCINST_);
create index IDX_TSKINST_TMINST on JBPM_TASKINSTANCE (TASKMGMTINSTANCE_);
create index IDX_TSKINST_SLINST on JBPM_TASKINSTANCE (SWIMLANINSTANCE_);
create index IDX_TASK_ACTORID on JBPM_TASKINSTANCE (ACTORID_);
create index IDX_TOKEN_PROCIN on JBPM_TOKEN (PROCESSINSTANCE_);
create index IDX_TOKEN_SUBPI on JBPM_TOKEN (SUBPROCESSINSTANCE_);
create index IDX_TOKEN_NODE on JBPM_TOKEN (NODE_);
create index IDX_TOKEN_PARENT on JBPM_TOKEN (PARENT_);
create index IDX_TKVARMAP_CTXT on JBPM_TOKENVARIABLEMAP (CONTEXTINSTANCE_);
create index IDX_TKVVARMP_TOKEN on JBPM_TOKENVARIABLEMAP (TOKEN_);
create index IDX_TRANSIT_TO on JBPM_TRANSITION (TO_);
create index IDX_TRANSIT_FROM on JBPM_TRANSITION (FROM_);
create index IDX_TRANS_PROCDEF on JBPM_TRANSITION (PROCESSDEFINITION_);
create index IDX_VARINST_TKVARMP on JBPM_VARIABLEINSTANCE (TOKENVARIABLEMAP_);
create index IDX_VARINST_PRCINS on JBPM_VARIABLEINSTANCE (PROCESSINSTANCE_);
create index IDX_VARINST_TK on JBPM_VARIABLEINSTANCE (TOKEN_);
alter table JBPM_ACTION add constraint FK_ACTION_EVENT foreign key (EVENT_) references JBPM_EVENT;
alter table JBPM_ACTION add constraint FK_ACTION_EXPTHDL foreign key (EXCEPTIONHANDLER_) references JBPM_EXCEPTIONHANDLER;
alter table JBPM_ACTION add constraint FK_ACTION_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_ACTION add constraint FK_CRTETIMERACT_TA foreign key (TIMERACTION_) references JBPM_ACTION;
alter table JBPM_ACTION add constraint FK_ACTION_ACTNDEL foreign key (ACTIONDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_ACTION add constraint FK_ACTION_REFACT foreign key (REFERENCEDACTION_) references JBPM_ACTION;
alter table JBPM_BYTEARRAY add constraint FK_BYTEARR_FILDEF foreign key (FILEDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_BYTEBLOCK add constraint FK_BYTEBLOCK_FILE foreign key (PROCESSFILE_) references JBPM_BYTEARRAY;
alter table JBPM_COMMENT add constraint FK_COMMENT_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_COMMENT add constraint FK_COMMENT_TSK foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_DECISIONCONDITIONS add constraint FK_DECCOND_DEC foreign key (DECISION_) references JBPM_NODE;
alter table JBPM_DELEGATION add constraint FK_DELEGATION_PRCD foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_EVENT add constraint FK_EVENT_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_EVENT add constraint FK_EVENT_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_EVENT add constraint FK_EVENT_TRANS foreign key (TRANSITION_) references JBPM_TRANSITION;
alter table JBPM_EVENT add constraint FK_EVENT_TASK foreign key (TASK_) references JBPM_TASK;
alter table JBPM_ID_GROUP add constraint FK_ID_GRP_PARENT foreign key (PARENT_) references JBPM_ID_GROUP;
alter table JBPM_ID_MEMBERSHIP add constraint FK_ID_MEMSHIP_GRP foreign key (GROUP_) references JBPM_ID_GROUP;
alter table JBPM_ID_MEMBERSHIP add constraint FK_ID_MEMSHIP_USR foreign key (USER_) references JBPM_ID_USER;
alter table JBPM_JOB add constraint FK_JOB_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_JOB add constraint FK_JOB_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_JOB add constraint FK_JOB_PRINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_JOB add constraint FK_JOB_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_JOB add constraint FK_JOB_TSKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_SOURCENODE foreign key (SOURCENODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_LOG add constraint FK_LOG_OLDBYTES foreign key (OLDBYTEARRAY_) references JBPM_BYTEARRAY;
alter table JBPM_LOG add constraint FK_LOG_NEWBYTES foreign key (NEWBYTEARRAY_) references JBPM_BYTEARRAY;
alter table JBPM_LOG add constraint FK_LOG_CHILDTOKEN foreign key (CHILD_) references JBPM_TOKEN;
alter table JBPM_LOG add constraint FK_LOG_DESTNODE foreign key (DESTINATIONNODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_TASKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_SWIMINST foreign key (SWIMLANEINSTANCE_) references JBPM_SWIMLANEINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_PARENT foreign key (PARENT_) references JBPM_LOG;
alter table JBPM_LOG add constraint FK_LOG_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_LOG add constraint FK_LOG_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_LOG add constraint FK_LOG_VARINST foreign key (VARIABLEINSTANCE_) references JBPM_VARIABLEINSTANCE;
alter table JBPM_LOG add constraint FK_LOG_TRANSITION foreign key (TRANSITION_) references JBPM_TRANSITION;
alter table JBPM_MODULEDEFINITION add constraint FK_TSKDEF_START foreign key (STARTTASK_) references JBPM_TASK;
alter table JBPM_MODULEDEFINITION add constraint FK_MODDEF_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_MODULEINSTANCE add constraint FK_TASKMGTINST_TMD foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_MODULEINSTANCE add constraint FK_MODINST_PRCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_NODE add constraint FK_PROCST_SBPRCDEF foreign key (SUBPROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_NODE add constraint FK_NODE_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_NODE add constraint FK_NODE_SCRIPT foreign key (SCRIPT_) references JBPM_ACTION;
alter table JBPM_NODE add constraint FK_NODE_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_NODE add constraint FK_DECISION_DELEG foreign key (DECISIONDELEGATION) references JBPM_DELEGATION;
alter table JBPM_NODE add constraint FK_NODE_SUPERSTATE foreign key (SUPERSTATE_) references JBPM_NODE;
alter table JBPM_POOLEDACTOR add constraint FK_POOLEDACTOR_SLI foreign key (SWIMLANEINSTANCE_) references JBPM_SWIMLANEINSTANCE;
alter table JBPM_PROCESSDEFINITION add constraint FK_PROCDEF_STRTSTA foreign key (STARTSTATE_) references JBPM_NODE;
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_ROOTTKN foreign key (ROOTTOKEN_) references JBPM_TOKEN;
alter table JBPM_PROCESSINSTANCE add constraint FK_PROCIN_SPROCTKN foreign key (SUPERPROCESSTOKEN_) references JBPM_TOKEN;
alter table JBPM_RUNTIMEACTION add constraint FK_RTACTN_PROCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_RUNTIMEACTION add constraint FK_RTACTN_ACTION foreign key (ACTION_) references JBPM_ACTION;
alter table JBPM_SWIMLANE add constraint FK_SWL_ASSDEL foreign key (ASSIGNMENTDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_SWIMLANE add constraint FK_SWL_TSKMGMTDEF foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_SWIMLANEINSTANCE add constraint FK_SWIMLANEINST_TM foreign key (TASKMGMTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_SWIMLANEINSTANCE add constraint FK_SWIMLANEINST_SL foreign key (SWIMLANE_) references JBPM_SWIMLANE;
alter table JBPM_TASK add constraint FK_TSK_TSKCTRL foreign key (TASKCONTROLLER_) references JBPM_TASKCONTROLLER;
alter table JBPM_TASK add constraint FK_TASK_ASSDEL foreign key (ASSIGNMENTDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_TASK add constraint FK_TASK_TASKNODE foreign key (TASKNODE_) references JBPM_NODE;
alter table JBPM_TASK add constraint FK_TASK_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_TASK add constraint FK_TASK_STARTST foreign key (STARTSTATE_) references JBPM_NODE;
alter table JBPM_TASK add constraint FK_TASK_TASKMGTDEF foreign key (TASKMGMTDEFINITION_) references JBPM_MODULEDEFINITION;
alter table JBPM_TASK add constraint FK_TASK_SWIMLANE foreign key (SWIMLANE_) references JBPM_SWIMLANE;
alter table JBPM_TASKACTORPOOL add constraint FK_TSKACTPOL_PLACT foreign key (POOLEDACTOR_) references JBPM_POOLEDACTOR;
alter table JBPM_TASKACTORPOOL add constraint FK_TASKACTPL_TSKI foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_TASKCONTROLLER add constraint FK_TSKCTRL_DELEG foreign key (TASKCONTROLLERDELEGATION_) references JBPM_DELEGATION;
alter table JBPM_TASKINSTANCE add constraint FK_TSKINS_PRCINS foreign key (PROCINST_) references JBPM_PROCESSINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TMINST foreign key (TASKMGMTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_SLINST foreign key (SWIMLANINSTANCE_) references JBPM_SWIMLANEINSTANCE;
alter table JBPM_TASKINSTANCE add constraint FK_TASKINST_TASK foreign key (TASK_) references JBPM_TASK;
alter table JBPM_TOKEN add constraint FK_TOKEN_PARENT foreign key (PARENT_) references JBPM_TOKEN;
alter table JBPM_TOKEN add constraint FK_TOKEN_NODE foreign key (NODE_) references JBPM_NODE;
alter table JBPM_TOKEN add constraint FK_TOKEN_PROCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_TOKEN add constraint FK_TOKEN_SUBPI foreign key (SUBPROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_TOKENVARIABLEMAP add constraint FK_TKVARMAP_CTXT foreign key (CONTEXTINSTANCE_) references JBPM_MODULEINSTANCE;
alter table JBPM_TOKENVARIABLEMAP add constraint FK_TKVARMAP_TOKEN foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_TRANSITION add constraint FK_TRANSITION_TO foreign key (TO_) references JBPM_NODE;
alter table JBPM_TRANSITION add constraint FK_TRANS_PROCDEF foreign key (PROCESSDEFINITION_) references JBPM_PROCESSDEFINITION;
alter table JBPM_TRANSITION add constraint FK_TRANSITION_FROM foreign key (FROM_) references JBPM_NODE;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_TSKCTRL foreign key (TASKCONTROLLER_) references JBPM_TASKCONTROLLER;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_SCRIPT foreign key (SCRIPT_) references JBPM_ACTION;
alter table JBPM_VARIABLEACCESS add constraint FK_VARACC_PROCST foreign key (PROCESSSTATE_) references JBPM_NODE;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_TK foreign key (TOKEN_) references JBPM_TOKEN;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_TKVARMP foreign key (TOKENVARIABLEMAP_) references JBPM_TOKENVARIABLEMAP;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VARINST_PRCINST foreign key (PROCESSINSTANCE_) references JBPM_PROCESSINSTANCE;
alter table JBPM_VARIABLEINSTANCE add constraint FK_VAR_TSKINST foreign key (TASKINSTANCE_) references JBPM_TASKINSTANCE;
alter table JBPM_VARIABLEINSTANCE add constraint FK_BYTEINST_ARRAY foreign key (BYTEARRAYVALUE_) references JBPM_BYTEARRAY;
create sequence hibernate_sequence;


commit;

GRANT DELETE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT INSERT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT UPDATE ON "CORPORATIVO"."CP_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_CONFIGURACAO_SEQ" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_GRUPO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."CP_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_ORGAO_USUARIO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_PAPEL" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_PERSONALIZACAO" TO "SIGAWF";
GRANT INSERT ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT UPDATE ON "CORPORATIVO"."CP_SERVICO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SERVICO_SEQ" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_SITUACAO_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_CONFIGURACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_GRUPO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_IDENTIDADE" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_LOTACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."CP_TIPO_PAPEL" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_CARGO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_FUNCAO_CONFIANCA" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."DP_LOTACAO" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_LOTACAO" TO "SIGAWF";
GRANT REFERENCES ON "CORPORATIVO"."DP_PESSOA" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_PESSOA" TO "SIGAWF";
GRANT SELECT ON "CORPORATIVO"."DP_SUBSTITUICAO" TO "SIGAWF";
GRANT "CONNECT" TO "SIGAWF";


/*FIM DOS GRANTS*/
