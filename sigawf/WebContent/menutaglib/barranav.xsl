<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml"  version="1.0" omit-xml-declaration="yes" />
    <xsl:param name="id" select="'home'"/>
    <xsl:strip-space elements="category"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="category[@id = $id]">
        <xsl:for-each select="ancestor::category">
            <a>
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
                <xsl:value-of select="@name"/>
            </a> &gt;
        </xsl:for-each>         
        <a>
            <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            <xsl:value-of select="@name"/>
        </a>
   <!--      <xsl:if test="count(category) &gt; 0">
            <ol>
                <xsl:for-each select="category">                 
                    <li>
                        <a>
                            <xsl:attribute name="href"><xsl:value-of select="@href"/>?idpage=<xsl:value-of select="@id"/></xsl:attribute>
                            <xsl:value-of select="@name"/>
                        </a> 
                    </li>
                </xsl:for-each>
            </ol>               
        </xsl:if>  -->
    </xsl:template>

</xsl:stylesheet>