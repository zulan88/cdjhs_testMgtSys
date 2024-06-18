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
 * <p>ParameterDeclarations enity
 * 
 *
 * 
 * <pre>
 * complexType name="ParameterDeclarations">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="ParameterDeclaration" type="{}ParameterDeclaration" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterDeclarations", propOrder = {
    "parameterDeclaration"
})
public class ParameterDeclarations {

    @XmlElement(name = "ParameterDeclaration")
    protected List<ParameterDeclaration> parameterDeclaration;

    /**
     * Gets the value of the parameterDeclaration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterDeclaration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterDeclaration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterDeclaration }
     * 
     * 
     */
    public List<ParameterDeclaration> getParameterDeclaration() {
        if (parameterDeclaration == null) {
            parameterDeclaration = new ArrayList<ParameterDeclaration>();
        }
        return this.parameterDeclaration;
    }

}
