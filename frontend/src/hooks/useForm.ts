import React, { useRef, useState } from 'react';

type Name = React.InputHTMLAttributes<HTMLInputElement>['name'];
type Value = React.InputHTMLAttributes<HTMLInputElement>['value'];
type Error = HTMLInputElement['validationMessage'];
type Values = Record<NonNullable<Name>, Value>;
type Errors = Record<NonNullable<Name>, Error>;
type Validation = {
  validate: (value: HTMLInputElement['value']) => boolean;
  validationMessage: HTMLInputElement['validationMessage'];
};
type InputAttributes = Omit<
  React.InputHTMLAttributes<HTMLInputElement>,
  'name'
> &
  Partial<{
    watch: boolean;
    patternValidationMessage: HTMLInputElement['validationMessage'];
    customValidations: Validation[];
  }>;
type InputController = Record<
  NonNullable<Name>,
  {
    element: HTMLInputElement;
    checkValidity: HTMLInputElement['checkValidity'];
  }
>;

const useForm = () => {
  const [values, setValues] = useState<Values>({});
  const [errors, setErrors] = useState<Errors>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const inputController = useRef<InputController>({});
  const inputElementList = useRef<HTMLInputElement[]>([]);

  const validateInput = (target: HTMLInputElement) => {
    const targetInputElement = inputController.current[target.name];

    targetInputElement.checkValidity();

    setErrors((prev) => ({
      ...prev,
      [target.name]: targetInputElement.element.validationMessage,
    }));
  };

  const handleChange =
    (
      onChange?: React.ChangeEventHandler<HTMLInputElement>
    ): React.ChangeEventHandler<HTMLInputElement> =>
    (e) => {
      const { currentTarget } = e;
      const { name, value } = currentTarget;

      validateInput(currentTarget);
      setValues((prev) => ({
        ...prev,
        [name]: value,
      }));
      onChange?.(e);
    };

  const handleBlur =
    (
      onBlur?: React.FocusEventHandler<HTMLInputElement>
    ): React.FocusEventHandler<HTMLInputElement> =>
    (e) => {
      const { currentTarget } = e;

      validateInput(currentTarget);
      onBlur?.(e);
    };

  const handleSubmit =
    (
      onValid: React.FormEventHandler<HTMLFormElement>,
      onError?: React.FormEventHandler<HTMLFormElement>
    ): React.FormEventHandler<HTMLFormElement> =>
    (e) => {
      e.preventDefault();

      inputElementList.current.forEach((element) => {
        validateInput(element);
      });

      const isValidateComplete =
        Object.keys(errors).length === inputElementList.current.length;
      const isValid = Object.values(errors).every(
        (errorMessage) => errorMessage === ''
      );

      setIsSubmitting(true);

      try {
        if (isValidateComplete && isValid) {
          onValid(e);

          return;
        }

        throw e;
      } catch (e) {
        onError?.(e as React.FormEvent<HTMLFormElement>);
      } finally {
        setIsSubmitting(false);
      }
    };

  const onSubmit = (
    onValid: React.FormEventHandler<HTMLFormElement>,
    onError?: React.FormEventHandler<HTMLFormElement>
  ) => {
    return {
      onSubmit: handleSubmit(onValid, onError),
      noValidate: true,
    };
  };

  const bindInputElementToRef =
    (
      patternValidationMessage: InputAttributes['patternValidationMessage'],
      customValidations: InputAttributes['customValidations']
    ): React.RefCallback<HTMLInputElement> =>
    (element) => {
      if (!element || inputController.current[element.name]) {
        return;
      }

      inputElementList.current.push(element);

      const setValidClass = () => {
        element.classList.add('valid');
        element.classList.remove('invalid');
      };

      const setInvalidClass = () => {
        element.classList.add('invalid');
        element.classList.remove('valid');
      };

      const checkCustomValidity = (
        customValidations: NonNullable<InputAttributes['customValidations']>
      ) =>
        customValidations.every(({ validate, validationMessage }) => {
          const customValidationValidity = validate(element.value);

          element.setCustomValidity(
            customValidationValidity ? '' : validationMessage
          );

          return customValidationValidity;
        });

      const checkValidity = () => {
        element.setCustomValidity('');

        let isValid = element.checkValidity();

        if (element.validity.patternMismatch && patternValidationMessage) {
          element.setCustomValidity(patternValidationMessage);
        }

        if (isValid && customValidations) {
          isValid = checkCustomValidity(customValidations);
        }

        if (isValid) {
          setValidClass();
        } else {
          setInvalidClass();
        }

        return isValid;
      };

      inputController.current[element.name] = {
        element,
        checkValidity,
      };
    };

  const register = (
    name: HTMLInputElement['name'],
    {
      watch,
      defaultValue,
      onChange,
      onBlur,
      patternValidationMessage,
      customValidations,
      ...attributes
    }: InputAttributes
  ) => {
    const shouldWatch =
      watch && !Object.prototype.hasOwnProperty.call(values, name);

    if (shouldWatch) {
      setValues((prev) => ({
        ...prev,
        [name]: defaultValue ?? '',
      }));
    }

    return {
      name,
      defaultValue,
      ref: bindInputElementToRef(patternValidationMessage, customValidations),
      onChange: handleChange(onChange),
      onBlur: handleBlur(onBlur),
      ...attributes,
    };
  };

  return { values, errors, isSubmitting, onSubmit, register };
};

export default useForm;
