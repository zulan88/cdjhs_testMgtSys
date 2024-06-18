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
 * <p>EntitySelection enity
 * 
 * 
 * 
 * <pre>
 * complexType name="EntitySelection">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Members" type="{}SelectedEntities"/>
 *       /sequence>
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntitySelection", propOrder = {
    "members"
})
public class EntitySelection {

    @XmlElement(name = "Members", required = true)
    protected SelectedEntities members;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡmembers���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SelectedEntities }
     *     
     */
    public SelectedEntities getMembers() {
        return members;
    }

    /**
     * ����members���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SelectedEntities }
     *     
     */
    public void setMembers(SelectedEntities value) {
        this.members = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
