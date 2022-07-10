import React, { useRef, useState } from 'react';

type Values = Record<HTMLInputElement['name'], HTMLInputElement['value']>;
type Errors = Record<HTMLInputElement['name'], string | null>;

const useForm = () => {
  const [values, setValues] = useState<Values>({});
  const [errors, setErrors] = useState<Errors>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const inputElementList = useRef<HTMLInputElement[]>([]);

  const validate = (target: HTMLInputElement) => {
    target.checkValidity();

    setErrors((prev) => ({
      ...prev,
      [target.name]: target.validity.valid ? null : target.validationMessage,
    }));
  };

  const handleChange: React.FormEventHandler<HTMLInputElement> = (e) => {
    const { target } = e;
    const { name, value } = target as HTMLInputElement;

    validate(target as HTMLInputElement);

    setValues((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleBlur: React.FormEventHandler<HTMLInputElement> = (e) => {
    const { target } = e;

    validate(target as HTMLInputElement);
  };

  const handleSubmit =
    (
      onValid: React.FormEventHandler<HTMLFormElement>,
      onError?: React.FormEventHandler<HTMLFormElement>
    ): React.FormEventHandler<HTMLFormElement> =>
    (e) => {
      e.preventDefault();

      inputElementList.current.forEach((element) => {
        validate(element);
      });

      const isValid = Object.values(errors).every(
        (errorMessage) => errorMessage === ''
      );

      setIsSubmitting(true);

      try {
        if (isValid) {
          onValid(e);

          return;
        }

        if (onError) {
          onError(e);
        }
      } catch (e) {
        console.error(e);
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

  const bindInputElementToRef: React.RefCallback<HTMLInputElement> = (
    element
  ) => {
    if (element && !inputElementList.current.includes(element)) {
      inputElementList.current.push(element);
    }
  };

  const register = (
    name: HTMLInputElement['name'],
    options: Omit<React.InputHTMLAttributes<HTMLInputElement>, 'name'>
  ) => {
    return {
      name,
      ...options,
      ref: bindInputElementToRef,
      onChange: handleChange,
      onBlur: handleBlur,
    };
  };

  return { values, errors, isSubmitting, onSubmit, register };
};

export default useForm;
