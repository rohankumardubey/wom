/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.jvnet.wom.impl.extension;

import org.jvnet.wom.WSDLExtension;
import org.jvnet.wom.binding.soap11.SOAPAddress;
import org.jvnet.wom.binding.soap11.SOAPBinding;
import org.jvnet.wom.parser.AbstractWSDLExtensionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;

/**
 * @author Vivek Pandey
 */
public class SOAPAddressExtensionHandler extends AbstractWSDLExtensionHandler {
    private final ContentHandler contentHandler = new SOAPAddressCH();
    private SOAPAddress address;

    public SOAPAddressExtensionHandler(ErrorHandler errorHandler, EntityResolver entityResolver) {
        super(errorHandler, entityResolver);
    }

    public WSDLExtension getExtension() {
        return address;
    }

    public QName extensibilityName() {
        return SOAPAddress.SOAPADDRESS_NAME;
    }

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    private class SOAPAddressCH extends WSDLExtensibilityContentHandler{
        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if(uri.equals(SOAPBinding.SOAP_NS) && localName.equals("address")){
                String location = atts.getValue("location");
                if(location == null){
                    errorHandler.error(new SAXParseException(Messages.format(Messages.MISSING_ATTR, "location", "soap:address"), locator));
                }
                address = new SOAPAddress();
                address.setLocation(location);
            }
        }
    }
}