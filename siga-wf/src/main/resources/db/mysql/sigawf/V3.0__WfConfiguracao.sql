--
-- Table structure for table wf_configuracao
--

DROP TABLE IF EXISTS wf_configuracao;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8 */;
CREATE TABLE wf_configuracao (
  CONF_ID bigint NOT NULL AUTO_INCREMENT,
  DEFP_ID bigint DEFAULT NULL,
  PRIMARY KEY (CONF_ID),
  KEY FK_DEFP (DEFP_ID),
  CONSTRAINT FK_DEFP FOREIGN KEY (DEFP_ID) REFERENCES sigawf.wf_def_procedimento (DEFP_ID)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
