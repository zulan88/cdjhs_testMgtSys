//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.*;


/**
 * <p>Init enity
 * 
 *
 * 
 * <pre>
 * complexType name="Init">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Actions" type="{}InitActions"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Init", propOrder = {
    "actions"
})
@XmlRootElement(name="Init")
public class Init {

    @XmlElement(name = "Actions", required = true)
    protected InitActions actions;

    /**
     * ��ȡactions���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link InitActions }
     *     
     */
    public InitActions getActions() {
        return actions;
    }

    /**
     * ����actions���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link InitActions }
     *     
     */
    public void setActions(InitActions value) {
        this.actions = value;
    }

}
