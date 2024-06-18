//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RouteCatalogLocation enity
 * 
 *
 * 
 * <pre>
 * complexType name="RouteCatalogLocation">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Directory" type="{}Directory"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteCatalogLocation", propOrder = {

})
public class RouteCatalogLocation {

    @XmlElement(name = "Directory", required = true)
    protected Directory directory;

    /**
     * ��ȡdirectory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Directory }
     *     
     */
    public Directory getDirectory() {
        return directory;
    }

    /**
     * ����directory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Directory }
     *     
     */
    public void setDirectory(Directory value) {
        this.directory = value;
    }

}
