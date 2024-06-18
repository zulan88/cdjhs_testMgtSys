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
 * <p>ObjectController enity
 * 
 *
 * 
 * <pre>
 * complexType name="ObjectController">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="CatalogReference" type="{}CatalogReference" minOccurs="0"/>
 *         element name="Controller" type="{}Controller" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectController", propOrder = {
    "catalogReference",
    "controller"
})
public class ObjectController {

    @XmlElement(name = "CatalogReference")
    protected CatalogReference catalogReference;
    @XmlElement(name = "Controller")
    protected Controller controller;

    /**
     * ��ȡcatalogReference���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CatalogReference }
     *     
     */
    public CatalogReference getCatalogReference() {
        return catalogReference;
    }

    /**
     * ����catalogReference���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogReference }
     *     
     */
    public void setCatalogReference(CatalogReference value) {
        this.catalogReference = value;
    }

    /**
     * ��ȡcontroller���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Controller }
     *     
     */
    public Controller getController() {
        return controller;
    }

    /**
     * ����controller���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Controller }
     *     
     */
    public void setController(Controller value) {
        this.controller = value;
    }

}
