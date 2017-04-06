/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2012-5-3 11:07:00                            */
/*==============================================================*/

/*==============================================================*/
/* Table: dee_cod_resourcetype                                  */
/*==============================================================*/
create table IF NOT EXISTS dee_cod_resourcetype
(
   type_id              varchar(32) not null,
   type_name            varchar(50) not null,
   primary key (type_id)
);

/*==============================================================*/
/* Table: dee_download                                          */
/*==============================================================*/
create table IF NOT EXISTS dee_download
(
   download_id          varchar(32) not null,
   resource_id          varchar(32) not null,
   fileneme             varchar(50) not null,
   content              text not null,
   primary key (download_id)
);

/*==============================================================*/
/* Table: dee_flow                                              */
/*==============================================================*/
create table IF NOT EXISTS dee_flow
(
   flow_id              varchar(32) not null,
   flow_type_id         varchar(32) not null,
   exetype_id           varchar(32),
   schedule_id          varchar(32),
   flow_name            varchar(50) not null,
   dis_name             varchar(50) not null,
   module_ids           varchar(50) not null,
   flow_desc            varchar(200),
   flow_meta            varchar(10000),
   ext1                 varchar(50),
   ext4                 varchar(50),
   ext3                 varchar(50),
   ext10                varchar(1000),
   ext9                 varchar(200),
   ext8                 varchar(200),
   ext7                 varchar(200),
   ext6                 varchar(200),
   ext2                 varchar(50),
   ext5                 varchar(50),
   primary key (flow_id)
);

/*==============================================================*/
/* Table: dee_flow_exetype                                      */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_exetype
(
   exetype_id           varchar(32) not null,
   exetype_name         varchar(50) not null,
   primary key (exetype_id)
);

/*==============================================================*/
/* Table: dee_flow_metadata                                     */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_metadata
(
   metadata_id          varchar(32) not null,
   flow_id              varchar(32) not null,
   metadata_code        text not null,
   primary key (metadata_id)
);

/*==============================================================*/
/* Table: dee_flow_module                                       */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_module
(
   module_id            varchar(32) not null,
   module_name          varchar(50) not null,
   service_flag         boolean not null,
   primary key (module_id)
);

/*==============================================================*/
/* Table: dee_flow_parameter                                    */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_parameter
(
   para_id              varchar(32) not null,
   flow_id              varchar(32) not null,
   para_name            varchar(50) not null,
   dis_name             varchar(50) not null,
   para_value           varchar(50) not null,
   para_desc            varchar(200),
   primary key (para_id)
);

/*==============================================================*/
/* Table: dee_flow_sub                                          */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_sub
(
   flow_sub_id          varchar(32) not null,
   flow_id              varchar(32) not null,
   resource_id          varchar(32) not null,
   sort                 int not null,
   primary key (flow_sub_id)
);

/*==============================================================*/
/* Table: dee_flow_type                                         */
/*==============================================================*/
create table IF NOT EXISTS dee_flow_type
(
   flow_type_id         varchar(32) not null,
   flow_type_name       varchar(50) not null,
   parent_id            varchar(32),
   flow_type_order      int(10),
   flow_type_desc       varchar(200),
   primary key (flow_type_id)
);

/*==============================================================*/
/* Table: dee_metaflow                                          */
/*==============================================================*/
create table IF NOT EXISTS dee_metaflow
(
   metaflow_id          varchar(32) not null,
   metaflow_name        varchar(50) not null,
   metaflow_code        text not null,
   primary key (metaflow_id)
);

/*==============================================================*/
/* Table: dee_sync_history                                      */
/*==============================================================*/
create table IF NOT EXISTS dee_sync_history
(
   sync_id              varchar(32) not null,
   sender_name          varchar(32),
   target_name          varchar(32),
   sync_mode            int,
   sync_state           int,
   sync_time            varchar(20),
   flow_id              varchar(32),
   primary key (sync_id)
);

/*==============================================================*/
/* Table: form_flow_history                                      */
/*==============================================================*/
create table IF NOT EXISTS form_flow_history
(
   flow_sync_id              varchar(32) not null,
   form_flow_id				 varchar(32),
   form_flow_name 			 varchar(255),
   operate_person 			 varchar(500),
   flow_action  			 varchar(255),
   ext1 					 varchar(50),
   ext2 					 varchar(50), 
   ext3 					 varchar(255), 
   ext4 					 varchar(255), 
   ext5 					 varchar(500),
   primary key (flow_sync_id)
);

/*==============================================================*/
/* Table: dee_redo                                              */
/*==============================================================*/
create table IF NOT EXISTS dee_redo
(
   redo_id              varchar(32) not null,
   redo_sid             int,
   writer_name          varchar(32),
   doc_code             text,
   para                 blob,
   counter              int,
   state_flag           char,
   flow_id              varchar(32),
   sync_id              varchar(32) not null,
   errormsg             varchar(2048),
   primary key (redo_id)
);

/*==============================================================*/
/* Table: dee_resource                                          */
/*==============================================================*/
create table IF NOT EXISTS dee_resource
(
   resource_id          varchar(32) not null,
   resource_template_id varchar(32) not null,
   resource_name        varchar(50) not null,
   dis_name             varchar(50) not null,
   resource_code        text not null,
   resource_desc        varchar(200),
   ref_id               varchar(32),
   ext1                 varchar(50),
   ext4                 varchar(50),
   ext3                 varchar(50),
   ext10                varchar(1000),
   ext9                 varchar(200),
   ext8                 varchar(200),
   ext7                 varchar(200),
   ext6                 varchar(200),
   ext2                 varchar(50),
   ext5                 varchar(50),
   primary key (resource_id)
);

/*==============================================================*/
/* Table: dee_resource_template                                 */
/*==============================================================*/
create table IF NOT EXISTS dee_resource_template
(
   resource_template_id varchar(32) not null,
   resource_template_name varchar(50) not null,
   type_id              varchar(32) not null,
   template             text,
   primary key (resource_template_id)
);

/*==============================================================*/
/* Table: dee_schedule                                          */
/*==============================================================*/
create table IF NOT EXISTS dee_schedule
(
   schedule_id          varchar(32) not null,
   schedule_name        varchar(50) not null,
   dis_name             varchar(50) not null,
   schedule_desc        varchar(200),
   schedule_code        varchar(1000),
   is_enable            boolean not null,
   quartz_code          varchar(200),
   flow_id              varchar(32),
   ext1                 varchar(50),
   ext4                 varchar(50),
   ext3                 varchar(50),
   ext10                varchar(1000),
   ext9                 varchar(200),
   ext8                 varchar(200),
   ext7                 varchar(200),
   ext6                 varchar(200),
   ext2                 varchar(50),
   ext5                 varchar(50),
   primary key (schedule_id)
);

/*==============================================================*/
/* Table: dee_code_package                                      */
/*==============================================================*/
CREATE TABLE IF NOT EXISTS dee_code_pkg
(
   name VARCHAR(400) NOT NULL,
   desc VARCHAR(50),
   PRIMARY KEY (name)
);

INSERT INTO dee_code_pkg (name, desc) VALUES ('com.seeyon.dee.codelib', NULL);

/*==============================================================*/
/* Table: dee_code_lib                                          */
/*==============================================================*/
CREATE TABLE IF NOT EXISTS dee_code_lib
(
   id          VARCHAR(50)  NOT NULL,
   class_name  VARCHAR(200) NOT NULL,
   pkg_name    VARCHAR(200) NOT NULL,
   simple_desc VARCHAR(500),
   code        TEXT         NOT NULL,
   create_time VARCHAR(20),
   modify_time VARCHAR(20),
   PRIMARY KEY (id)
);

--alter table dee_redo add column sync_id varchar(32);
alter table dee_redo add constraint IF NOT EXISTS FK_Reference_11 foreign key (sync_id)
      references dee_sync_history (sync_id) on delete restrict on update restrict;

alter table dee_download add constraint IF NOT EXISTS FK_Reference_7 foreign key (resource_id)
      references dee_resource (resource_id) on delete restrict on update restrict;

alter table dee_flow add constraint IF NOT EXISTS FK_Reference_8 foreign key (flow_type_id)
      references dee_flow_type (flow_type_id) on delete restrict on update restrict;

alter table dee_flow_metadata add constraint IF NOT EXISTS FK_Reference_12 foreign key (flow_id)
      references dee_flow (flow_id) on delete restrict on update restrict;

alter table dee_flow_parameter add constraint IF NOT EXISTS FK_Reference_13 foreign key (flow_id)
      references dee_flow (flow_id) on delete restrict on update restrict;

alter table dee_flow_sub add constraint IF NOT EXISTS FK_Reference_10 foreign key (flow_id)
      references dee_flow (flow_id) on delete restrict on update restrict;

--alter table dee_resource add constraint IF NOT EXISTS FK_Reference_14 foreign key (resource_id)
--      references dee_flow_sub (resource_id) on delete restrict on update restrict;

alter table dee_resource add constraint IF NOT EXISTS FK_Reference_6 foreign key (resource_template_id)
      references dee_resource_template (resource_template_id) on delete restrict on update restrict;

alter table dee_resource_template add constraint IF NOT EXISTS FK_Reference_9 foreign key (type_id)
      references dee_cod_resourcetype (type_id) on delete restrict on update restrict;

alter table dee_schedule add constraint IF NOT EXISTS FK_Reference_16 foreign key (flow_id)
      references dee_flow (flow_id) on delete restrict on update restrict;

INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('0','Reader');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('1','Writer');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('2','Processor');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('3','Database');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('4','Dictionary');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('5','Script');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('6','Mapping');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('7','Dictionary');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('8','Listener');
INSERT INTO DEE_COD_RESOURCETYPE (TYPE_ID,TYPE_NAME) VALUES ('9','Function');

INSERT INTO DEE_FLOW_EXETYPE (EXETYPE_ID,EXETYPE_NAME) VALUES ('0','查询类交换任务');
INSERT INTO DEE_FLOW_EXETYPE (EXETYPE_ID,EXETYPE_NAME) VALUES ('1','执行类交换任务');

INSERT INTO DEE_FLOW_MODULE (MODULE_ID,MODULE_NAME,SERVICE_FLAG) VALUES ('10000','A8表单控件',1);
INSERT INTO DEE_FLOW_MODULE (MODULE_ID,MODULE_NAME,SERVICE_FLAG) VALUES ('10001','A8数据触发',1);
INSERT INTO DEE_FLOW_MODULE (MODULE_ID,MODULE_NAME,SERVICE_FLAG) VALUES ('10002','Portal栏目',1);
INSERT INTO DEE_FLOW_MODULE (MODULE_ID,MODULE_NAME,SERVICE_FLAG) VALUES ('10003','其它',1);
INSERT INTO DEE_FLOW_MODULE (MODULE_ID,MODULE_NAME,SERVICE_FLAG) VALUES ('10004','NC-OA',1);

-- INSERT INTO DEE_METAFLOW (METAFLOW_ID,METAFLOW_NAME,METAFLOW_CODE) VALUES ('002','查询表单','<flow name="fetch-001"><adapter name="jdbcReader-001" class="com.seeyon.v3x.dee.adapter.JDBCReader"><description>读取购买申请单数据</description><property name="dataSource" ref="ds1"/><map name="sql"><key name="po_order" value="select appname,datadefine from form_appmain"/></map></adapter></flow>');
-- DELETE FROM DEE_METAFLOW;
INSERT INTO DEE_FLOW_TYPE (FLOW_TYPE_ID,FLOW_TYPE_NAME,PARENT_ID,FLOW_TYPE_ORDER,FLOW_TYPE_DESC) VALUES ('0','交换任务分类','-1',4,null);

INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('0','JDBCReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('1','XMLReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('2','JDBCWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('3','ColumnMappingProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('4','XSLTProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('5','JDBCDatasource','3','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('6','ExchangeMapping','6','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('7','A8WSGetTokenProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('8','A8BPMLauchFormColWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('9','ScriptProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('10','JNDIDataSource','3','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('11','XMLSchemaValidateProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('12','JDBCDictionary','7','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('13','StaticDictionary','7','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('14','ReaderScript','5','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('15','ProcessorScript','5','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('16','WriterScript','5','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('17','CustomReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('18','CustomProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('19','CustomWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('20','A8CommonWSWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('21','WSReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('22','WSProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('23','WSWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('24','SyncListener','8','<listener class="com.seeyon.v3x.dee.common.listener.SyncListener"/>');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('25','SapJcoReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('26','SapJcoProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('27','SapJcoWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('28','OrgSyncWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('29','SystemFunction','9','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('30','SAPWSReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('31','SAPWSProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('32','SAPWSWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('33','A8MetaDatasource','3','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('34','A8EnumReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('35','A8EnumWriter','1','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES('36', 'RestReader', '0', 's');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES('37', 'RestWriter', '1', 's');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES('38', 'A8MsgWriter', '1', 's');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES('39', 'A8FormWriteBackWriter', '1', 's');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('40','SRReader','0','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('41','SRProcessor','2','s');
INSERT INTO DEE_RESOURCE_TEMPLATE (RESOURCE_TEMPLATE_ID,RESOURCE_TEMPLATE_NAME,TYPE_ID,TEMPLATE) VALUES ('42','SRWriter','1','s');
INSERT INTO DEE_RESOURCE (RESOURCE_ID,RESOURCE_TEMPLATE_ID,RESOURCE_NAME,RESOURCE_CODE,RESOURCE_DESC,DIS_NAME) VALUES ('201211151047','29','uuid','','生成通用唯一识别编码','uuid');
