package ezvcard.io.scribe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.mangstadt.vinnie.io.VObjectPropertyValues;

import ezvcard.VCard;
import ezvcard.VCardDataType;
import ezvcard.VCardVersion;
import ezvcard.io.CannotParseException;
import ezvcard.io.ParseContext;
import ezvcard.io.json.JCardValue;
import ezvcard.io.text.WriteContext;
import ezvcard.io.xml.XCardElement;
import ezvcard.parameter.VCardParameters;
import ezvcard.property.Impp;

/*
 Copyright (c) 2012-2018, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Marshals {@link Impp} properties.
 * @author Michael Angstadt
 */
public class ImppScribe extends VCardPropertyScribe<Impp> {
	public static final String AIM = "aim";
	public static final String ICQ = "icq";
	public static final String IRC = "irc";
	public static final String MSN = "msnim";
	public static final String SIP = "sip";
	public static final String SKYPE = "skype";
	public static final String XMPP = "xmpp";
	public static final String YAHOO = "ymsgr";

	public ImppScribe() {
		super(Impp.class, "IMPP");
	}

	@Override
	protected void _prepareParameters(Impp property, VCardParameters copy, VCardVersion version, VCard vcard) {
		handlePrefParam(property, copy, version, vcard);
	}

	@Override
	protected VCardDataType _defaultDataType(VCardVersion version) {
		return VCardDataType.URI;
	}

	@Override
	protected String _writeText(Impp property, WriteContext context) {
		return write(property);
	}

	@Override
	protected Impp _parseText(String value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		value = VObjectPropertyValues.unescape(value);
		return parse(value);
	}

	@Override
	protected void _writeXml(Impp property, XCardElement parent) {
		parent.append(VCardDataType.URI, write(property));
	}

	@Override
	protected Impp _parseXml(XCardElement element, VCardParameters parameters, ParseContext context) {
		String value = element.first(VCardDataType.URI);
		if (value != null) {
			return parse(value);
		}

		throw missingXmlElements(VCardDataType.URI);
	}

	@Override
	protected JCardValue _writeJson(Impp property) {
		return JCardValue.single(write(property));
	}

	@Override
	protected Impp _parseJson(JCardValue value, VCardDataType dataType, VCardParameters parameters, ParseContext context) {
		return parse(value.asSingle());
	}

	private String write(Impp property) {
		URI uri = property.getUri();
		return (uri == null) ? "" : uri.toASCIIString();
	}

	private Impp parse(String value) {
		if (value == null || value.length() == 0) {
			return new Impp((URI) null);
		}

		try {
			return new Impp(value);
		} catch (IllegalArgumentException e) {
			throw new CannotParseException(15, value, e.getMessage());
		}
	}
}
