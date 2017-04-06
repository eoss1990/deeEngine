<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:StringUtils="xalan://org.apache.commons.lang.StringUtils">
	<xsl:output method="xml" encoding="utf-8"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="root">
    <xsl:element name="DataPojo">
      <xsl:attribute name="type">IEOrganizationInfo</xsl:attribute>
      <xsl:attribute name="version">1</xsl:attribute>
      <xsl:attribute name="isnull">false</xsl:attribute>
      <xsl:attribute name="valuecount"><xsl:value-of select="count(*)"/></xsl:attribute>
			<xsl:for-each select="node()">
				  <xsl:if test="StringUtils:lowerCase(name())='deparray'">
            		<xsl:element name="DataProperty">
            			<xsl:attribute name="propertyname">depArray</xsl:attribute>
            			<xsl:attribute name="valuetype">10</xsl:attribute>
            			<xsl:attribute name="isnull">false</xsl:attribute>
            			<xsl:attribute name="length"><xsl:value-of select="count(*)"/></xsl:attribute>
                		<xsl:apply-templates select="." mode="dept"/>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='ocupationarray'">
            		<xsl:element name="DataProperty">
            			<xsl:attribute name="propertyname">ocupationArray</xsl:attribute>
            			<xsl:attribute name="valuetype">10</xsl:attribute>
            			<xsl:attribute name="isnull">false</xsl:attribute>
            			<xsl:attribute name="length"><xsl:value-of select="count(*)"/></xsl:attribute>
                		<xsl:apply-templates select="." mode="ocu"/>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='otypearray'">
            		<xsl:element name="DataProperty">
            			<xsl:attribute name="propertyname">otypeArray</xsl:attribute>
            			<xsl:attribute name="valuetype">10</xsl:attribute>
            			<xsl:attribute name="isnull">false</xsl:attribute>
            			<xsl:attribute name="length"><xsl:value-of select="count(*)"/></xsl:attribute>
                		<xsl:apply-templates select="." mode="otype"/>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='personarray'">
            		<xsl:element name="DataProperty">
            			<xsl:attribute name="propertyname">personArray</xsl:attribute>
            			<xsl:attribute name="valuetype">10</xsl:attribute>
            			<xsl:attribute name="isnull">false</xsl:attribute>
            			<xsl:attribute name="length"><xsl:value-of select="count(*)"/></xsl:attribute>
                		<xsl:apply-templates select="." mode="person"/>
            		</xsl:element>
				  </xsl:if>
			</xsl:for-each>
    </xsl:element>
	</xsl:template>
	<xsl:template match="row" mode="dept">
    <DataPojo type="DepartmentInfoParam_All"  version="1"  valuecount="5"  isnull="false" >
  			<DataProperty propertyname="accountId"  valuetype="3"  value="dee670869647114347dee" />
			<xsl:for-each select="node()">
				  <xsl:if test="StringUtils:lowerCase(name())='discursion'">
            		<DataProperty propertyname="discursion"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='departmentname'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">departmentName</xsl:attribute>
              			<xsl:attribute name="valuetype">7</xsl:attribute>
              			<xsl:attribute name="isnull">false</xsl:attribute>
              			<xsl:attribute name="length">1</xsl:attribute>
              			<DataValue isnull="false" ><xsl:value-of select="text()"/></DataValue>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='dep_sort'">
            		<DataProperty propertyname="dep_sort"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='departmentnumber'">
            		<DataProperty propertyname="departmentNumber"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
			</xsl:for-each>
		</DataPojo>
	</xsl:template>
	<xsl:template match="row" mode="ocu">
    <DataPojo type="OcupationInfoParam_A8_All"  version="1"  valuecount="7"  isnull="false" >
  			<DataProperty propertyname="accountId"  valuetype="3"  value="dee670869647114347dee" />
			<xsl:for-each select="node()">
				  <xsl:if test="StringUtils:lowerCase(name())='ocupationname'">
            		<DataProperty propertyname="ocupationName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='sortid'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">sortId</xsl:attribute>
              			<xsl:attribute name="valuetype">0</xsl:attribute>
              			<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='discursion'">
            		<DataProperty propertyname="discursion"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='code'">
            		<DataProperty propertyname="code"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='type'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">type</xsl:attribute>
              			<xsl:attribute name="valuetype">3</xsl:attribute>
              			<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
            		</xsl:element>
				  </xsl:if>
			</xsl:for-each>
  			<DataProperty propertyname="departmentArray"  valuetype="10"  value=""  isnull="false"  length="0" />
		</DataPojo>
	</xsl:template>
	<xsl:template match="row" mode="otype">
    <DataPojo type="OtypeInfoParam_A8_All"  version="1"  valuecount="6"  isnull="false" >
  			<DataProperty propertyname="accountId"  valuetype="3"  value="dee670869647114347dee" />
			<xsl:for-each select="node()">
				  <xsl:if test="StringUtils:lowerCase(name())='levelid'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">levelId</xsl:attribute>
              			<xsl:attribute name="valuetype">0</xsl:attribute>
              			<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='discursion'">
            		<DataProperty propertyname="discursion"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='code'">
            		<DataProperty propertyname="code"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='otypename'">
            		<DataProperty propertyname="OTypeName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
			</xsl:for-each>
  			<DataProperty propertyname="parentName"  valuetype="1"  isnull="true" />
		</DataPojo>
	</xsl:template>
	<xsl:template match="row" mode="person">
    <DataPojo type="PersonInfoParam_All"  version="1"  valuecount="20"  isnull="false" >
  			<DataProperty propertyname="accountId"  valuetype="3"  value="dee670869647114347dee" />
			<xsl:for-each select="node()">
				  <xsl:if test="StringUtils:lowerCase(name())='otypename'">
            		<DataProperty propertyname="otypeName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='birthday'">
            		<DataProperty propertyname="birthday"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='per_sort'">
            		<DataProperty propertyname="per_sort"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='sex'">
            		<DataProperty propertyname="sex"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='ocupationname'">
            		<DataProperty propertyname="ocupationName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='secondocupationname'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">secondOcupationName</xsl:attribute>
              			<xsl:attribute name="valuetype">7</xsl:attribute>
              			<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
              			<xsl:attribute name="isnull">false</xsl:attribute>
              			<xsl:attribute name="length"><xsl:value-of select="count(*)"/></xsl:attribute>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='truename'">
            		<DataProperty propertyname="trueName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='discursion'">
            		<DataProperty propertyname="discursion"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='familyphone'">
            		<DataProperty propertyname="familyPhone"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='officephone'">
            		<DataProperty propertyname="officePhone"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='departmentname'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">departmentName</xsl:attribute>
              			<xsl:attribute name="valuetype">7</xsl:attribute>
              			<xsl:attribute name="isnull">false</xsl:attribute>
              			<xsl:attribute name="length">1</xsl:attribute>
              			<DataValue isnull="false" ><xsl:value-of select="text()"/></DataValue>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='password'">
            		<DataProperty propertyname="passWord"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='staffnumber'">
            		<DataProperty propertyname="staffNumber"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='familyaddress'">
            		<DataProperty propertyname="familyAddress"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='id'">
            		<xsl:element name="DataProperty">
              			<xsl:attribute name="propertyname">id</xsl:attribute>
              			<xsl:attribute name="valuetype">3</xsl:attribute>
              			<xsl:attribute name="value"><xsl:value-of select="text()"/></xsl:attribute>
            		</xsl:element>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='identity'">
            		<DataProperty propertyname="identity"  valuetype="1"  isnull="true" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='mobilephone'">
           			<DataProperty propertyname="mobilePhone"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='email'">
            		<DataProperty propertyname="email"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
				  <xsl:if test="StringUtils:lowerCase(name())='loginname'">
            		<DataProperty propertyname="loginName"  valuetype="1"  isnull="false" ><xsl:value-of select="text()"/></DataProperty>
				  </xsl:if>
			</xsl:for-each>
		</DataPojo>
	</xsl:template>
</xsl:stylesheet>
