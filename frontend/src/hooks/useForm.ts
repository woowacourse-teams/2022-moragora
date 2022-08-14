import React, { useRef, useState } from 'react';

type Name = React.InputHTMLAttributes<HTMLInputElement>['name'];
type Value = React.InputHTMLAttributes<HTMLInputElement>['value'];
type Error = HTMLInputElement['validationMessage'];
type Values = Record<NonNullable<Name>, Value>;
type Errors = Record<NonNullable<Name>, Error>;
type Validation = {
  validate: (
    value: HTMLInputElement['value'],
    inputController: InputController
  ) => boolean;
  validationMessage: HTMLInputElement['validationMessage'];
};
type InputAttributes = Omit<
  React.InputHTMLAttributes<HTMLInputElement>,
  'name' | 'onChange' | 'onBlur'
> &
  Partial<{
    watch: boolean;
    onChange: (
      event: React.ChangeEvent<HTMLInputElement>,
      inputController: InputController
    ) => ReturnType<React.ChangeEventHandler<HTMLInputElement>>;
    onBlur: (
      event: React.FocusEvent<HTMLInputElement>,
      inputController: InputController
    ) => ReturnType<React.FocusEventHandler<HTMLInputElement>>;
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
  const inputControllerRef = useRef<InputController>({});
  const inputElementList = useRef<HTMLInputElement[]>([]);

  const handleChange =
    (
      inputController: InputController,
      onChange: InputAttributes['onChange']
    ): React.ChangeEventHandler<HTMLInputElement> =>
    (e) => {
      const { currentTarget } = e;

      inputController[currentTarget.name].checkValidity();

      setValues((prev) => ({
        ...prev,
        [currentTarget.name]: currentTarget.value,
      }));
      onChange?.(e, inputController);
    };

  const handleBlur =
    (
      inputController: InputController,
      onBlur: InputAttributes['onBlur']
    ): React.FocusEventHandler<HTMLInputElement> =>
    (e) => {
      const { currentTarget } = e;

      inputController[currentTarget.name].checkValidity();
      onBlur?.(e, inputController);
    };

  const handleSubmit =
    (
      onValid:
        | React.FormEventHandler<HTMLFormElement>
        | ((event: React.FormEvent<HTMLFormElement>) => Promise<void>),
      onError?:
        | React.FormEventHandler<HTMLFormElement>
        | ((event: React.FormEvent<HTMLFormElement>) => Promise<void>)
    ): React.FormEventHandler<HTMLFormElement> =>
    async (e) => {
      e.preventDefault();

      inputElementList.current.forEach((element) => {
        inputControllerRef.current[element.name].checkValidity();
      });

      const isValidateComplete =
        Object.keys(errors).length === inputElementList.current.length;
      const isValid = Object.values(errors).every(
        (errorMessage) => errorMessage === ''
      );

      setIsSubmitting(true);

      try {
        if (isValidateComplete && isValid) {
          await onValid(e);

          return;
        }

        throw e;
      } catch (e) {
        await onError?.(e as React.FormEvent<HTMLFormElement>);
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
      if (!element || inputControllerRef.current[element.name]) {
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
          const customValidationValidity = validate(
            element.value,
            inputControllerRef.current
          );

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

        setErrors((prev) => ({
          ...prev,
          [element.name]: element.validationMessage,
        }));

        return isValid;
      };

      inputControllerRef.current[element.name] = {
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
      onChange: handleChange(inputControllerRef.current, onChange),
      onBlur: handleBlur(inputControllerRef.current, onBlur),
      ...attributes,
    };
  };

  return {
    values,
    errors,
    isSubmitting,
    inputController: inputControllerRef.current,
    onSubmit,
    register,
  };
};

export default useForm;
