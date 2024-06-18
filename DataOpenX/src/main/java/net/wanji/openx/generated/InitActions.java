//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>InitActions enity
 * 
 * 
 * 
 * <pre>
 * complexType name="InitActions">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="GlobalAction" type="{}GlobalAction" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="UserDefinedAction" type="{}UserDefinedAction" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Private" type="{}Private" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InitActions", propOrder = {
    "globalAction",
    "userDefinedAction",
    "_private"
})
public class InitActions {

    @XmlElement(name = "GlobalAction")
    protected List<GlobalAction> globalAction;
    @XmlElement(name = "UserDefinedAction")
    protected List<UserDefinedAction> userDefinedAction;
    @XmlElement(name = "Private")
    protected List<Private> _private;

    /**
     * Gets the value of the globalAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the globalAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlobalAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GlobalAction }
     * 
     * 
     */
    public List<GlobalAction> getGlobalAction() {
        if (globalAction == null) {
            globalAction = new ArrayList<GlobalAction>();
        }
        return this.globalAction;
    }

    /**
     * Gets the value of the userDefinedAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userDefinedAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDefinedAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDefinedAction }
     * 
     * 
     */
    public List<UserDefinedAction> getUserDefinedAction() {
        if (userDefinedAction == null) {
            userDefinedAction = new ArrayList<UserDefinedAction>();
        }
        return this.userDefinedAction;
    }

    /**
     * Gets the value of the private property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the private property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrivate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Private }
     * 
     * 
     */
    public List<Private> getPrivate() {
        if (_private == null) {
            _private = new ArrayList<Private>();
        }
        return this._private;
    }

}
