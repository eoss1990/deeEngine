<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="utf-8"/>
	<xsl:strip-space elements="*"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	<xsl:template match="root">
		<formExport version="2.0">
			<summary id="" name=""/>
			<xsl:apply-templates select="node()[position()=1]" mode="master"/>
			<subForms>
				<xsl:for-each select="node()">
					<xsl:if test="position()!=1">
						<xsl:element name="subForm">
							<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
							<values>
								<xsl:apply-templates select="." mode="slave"/>
							</values>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
			</subForms>
		</formExport>
	</xsl:template>
	<xsl:template match="*" mode="master">
		<xsl:apply-templates select="row"/>
	</xsl:template>
	<xsl:template match="row" mode="slave">
		<row>
			<xsl:for-each select="node()">
				<xsl:element name="column">
					<xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
					<value>
						<xsl:value-of select="text()"/>
					</value>
				</xsl:element>
			</xsl:for-each>
		</row>
	</xsl:template>
	<xsl:template match="row">
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
</xsl:stylesheet>
