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
 * <p>EntityAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="EntityAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="AddEntityAction" type="{}AddEntityAction" minOccurs="0"/>
 *         element name="DeleteEntityAction" type="{}DeleteEntityAction" minOccurs="0"/>
 *       /choice>
 *       attribute name="entityRef" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityAction", propOrder = {
    "addEntityAction",
    "deleteEntityAction"
})
public class EntityAction {

    @XmlElement(name = "AddEntityAction")
    protected AddEntityAction addEntityAction;
    @XmlElement(name = "DeleteEntityAction")
    protected DeleteEntityAction deleteEntityAction;
    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;

    /**
     * ��ȡaddEntityAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AddEntityAction }
     *     
     */
    public AddEntityAction getAddEntityAction() {
        return addEntityAction;
    }

    /**
     * ����addEntityAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AddEntityAction }
     *     
     */
    public void setAddEntityAction(AddEntityAction value) {
        this.addEntityAction = value;
    }

    /**
     * ��ȡdeleteEntityAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link DeleteEntityAction }
     *     
     */
    public DeleteEntityAction getDeleteEntityAction() {
        return deleteEntityAction;
    }

    /**
     * ����deleteEntityAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteEntityAction }
     *     
     */
    public void setDeleteEntityAction(DeleteEntityAction value) {
        this.deleteEntityAction = value;
    }

    /**
     * ��ȡentityRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityRef() {
        return entityRef;
    }

    /**
     * ����entityRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityRef(String value) {
        this.entityRef = value;
    }

}
