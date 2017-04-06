<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="utf-8"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="root">
		<forms version="2.1">
			<formExport>
				<summary id="" name=""/>
				<xsl:apply-templates select="node()[position()=1]" mode="master"/>
				<subForms>
					<xsl:for-each select="node()">
						<xsl:if test="position()!=1">
							<xsl:element name="subForm">
								<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
								<xsl:apply-templates select="." mode="slave"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</subForms>
			</formExport>
		</forms>
	</xsl:template>
	<xsl:template match="*" mode="master">
		<xsl:apply-templates select="row" mode="main"/>
	</xsl:template>
	<xsl:template match="*" mode="slave">
		<xsl:apply-templates select="row" mode="son"/>
	</xsl:template>
	<xsl:template match="row" mode="main">
		<values>
			<xsl:for-each select="node()">
				<xsl:if test="name()!=''">
					<xsl:element name="column">
						<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
						<value>
							<xsl:value-of select="text()"/>
						</value>
					</xsl:element>
				</xsl:if>
			</xsl:for-each>
		</values>
	</xsl:template>
	<xsl:template match="row" mode="son">
		<values>
			<row>
				<xsl:for-each select="node()">
					<xsl:if test="name()!=''">
						<xsl:element name="column">
							<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
							<value>
								<xsl:value-of select="text()"/>
							</value>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
			</row>
		</values>
	</xsl:template>
</xsl:stylesheet>
