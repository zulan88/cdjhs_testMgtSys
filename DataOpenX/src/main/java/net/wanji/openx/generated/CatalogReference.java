//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>CatalogReference enity
 * 
 *
 * 
 * <pre>
 * complexType name="CatalogReference">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="ParameterAssignments" type="{}ParameterAssignments" minOccurs="0"/>
 *       /sequence>
 *       attribute name="catalogName" use="required" type="{}String" />
 *       attribute name="entryName" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CatalogReference", propOrder = {
    "parameterAssignments"
})
public class CatalogReference {

    @XmlElement(name = "ParameterAssignments")
    protected ParameterAssignments parameterAssignments;
    @XmlAttribute(name = "catalogName", required = true)
    protected String catalogName;
    @XmlAttribute(name = "entryName", required = true)
    protected String entryName;

    /**
     * ��ȡparameterAssignments���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterAssignments }
     *     
     */
    public ParameterAssignments getParameterAssignments() {
        return parameterAssignments;
    }

    /**
     * ����parameterAssignments���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterAssignments }
     *     
     */
    public void setParameterAssignments(ParameterAssignments value) {
        this.parameterAssignments = value;
    }

    /**
     * ��ȡcatalogName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * ����catalogName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCatalogName(String value) {
        this.catalogName = value;
    }

    /**
     * ��ȡentryName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryName() {
        return entryName;
    }

    /**
     * ����entryName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryName(String value) {
        this.entryName = value;
    }

}
