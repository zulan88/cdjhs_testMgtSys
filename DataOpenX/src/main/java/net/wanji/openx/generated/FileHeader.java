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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>FileHeader enity
 * 
 *
 * 
 * <pre>
 * complexType name="FileHeader">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="author" use="required" type="{}String" />
 *       attribute name="date" use="required" type="{}DateTime" />
 *       attribute name="description" use="required" type="{}String" />
 *       attribute name="revMajor" use="required" type="{}UnsignedShort" />
 *       attribute name="revMinor" use="required" type="{}UnsignedShort" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileHeader")
public class FileHeader {

    @XmlAttribute(name = "author", required = true)
    protected String author;
    @XmlAttribute(name = "date", required = true)
    protected String date;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "revMajor", required = true)
    protected String revMajor;
    @XmlAttribute(name = "revMinor", required = true)
    protected String revMinor;

    /**
     * ��ȡauthor���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * ����author���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * ��ȡdate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * ����date���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * ��ȡdescription���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * ����description���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * ��ȡrevMajor���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevMajor() {
        return revMajor;
    }

    /**
     * ����revMajor���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevMajor(String value) {
        this.revMajor = value;
    }

    /**
     * ��ȡrevMinor���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevMinor() {
        return revMinor;
    }

    /**
     * ����revMinor���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevMinor(String value) {
        this.revMinor = value;
    }

}
