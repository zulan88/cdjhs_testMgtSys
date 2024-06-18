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
 * <p>EntityRef enity
 * 
 * 
 * 
 * <pre>
 * complexType name="EntityRef">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="entityRef" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityRef")
public class EntityRef {

    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;

    public String getEntityRef() {
        return entityRef;
    }

    public void setEntityRef(String value) {
        this.entityRef = value;
    }

}
