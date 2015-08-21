package ca.uhn.fhir.jpa.provider;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;

public class BaseJpaResourceProviderValueSetDstu2 extends JpaResourceProviderDstu2<ValueSet> {

	//@formatter:off
	@Operation(name = "$expand", idempotent = true)
	public ValueSet everything(
			HttpServletRequest theServletRequest,
			@IdParam IdDt theId, 
			@OperationParam(name = "filter") StringDt theFilter) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			IFhirResourceDaoValueSet<ValueSet> dao = (IFhirResourceDaoValueSet<ValueSet>) getDao();
			return dao.expand(theId, theFilter);
		} finally {
			endRequest(theServletRequest);
		}
	}

	//@formatter:off
	@Operation(name = "$validate-code", idempotent = true, returnParameters= {
		@OperationParam(name="result", type=BooleanDt.class, min=1),
		@OperationParam(name="message", type=StringDt.class),
		@OperationParam(name="display", type=StringDt.class)
	})
	public Parameters everything(
			HttpServletRequest theServletRequest,
			@IdParam IdDt theId, 
			@OperationParam(name="identifier") UriDt theValueSetIdentifier, 
			@OperationParam(name="code") CodeDt theCode, 
			@OperationParam(name="system") UriDt theSystem,
			@OperationParam(name="display") StringDt theDisplay,
			@OperationParam(name="coding") CodingDt theCoding,
			@OperationParam(name="codeableConcept") CodeableConceptDt theCodeableConcept
			) {
		//@formatter:on
		
		startRequest(theServletRequest);
		try {
			IFhirResourceDaoValueSet<ValueSet> dao = (IFhirResourceDaoValueSet<ValueSet>) getDao();
			ValidateCodeResult result = dao.validateCode(theValueSetIdentifier, theId, theCode, theSystem, theDisplay, theCoding, theCodeableConcept);
			Parameters retVal = new Parameters();
			retVal.addParameter().setName("result").setValue(new BooleanDt(result.isResult()));
			if (isNotBlank(result.getMessage())) {
				retVal.addParameter().setName("message").setValue(new StringDt(result.getMessage()));
			}
			if (isNotBlank(result.getDisplay())) {
				retVal.addParameter().setName("display").setValue(new StringDt(result.getDisplay()));
			}
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}
}