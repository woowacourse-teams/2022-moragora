import { css } from '@emotion/react';
import NicknameInput from '.';
import useForm from 'hooks/useForm';

export default {
  title: 'Components/NicknameInput',
  component: NicknameInput,
};

const Template = (args) => {
  const { values, onSubmit, register, isSubmitting } = useForm();

  const handleValid = (e) => {
    console.log(values['nickname']);
  };

  const handleError = (e) => {
    console.log(e.target);
  };

  return (
    <div
      css={css`
        width: 20rem;
      `}
    >
      <form {...onSubmit(handleValid, handleError)}>
        <NicknameInput
          type="text"
          {...register('nickname', {
            defaultValue: 'unknown',
            onBlur: ({ target }) => {
              target.form?.requestSubmit();
            },
            minLength: 1,
            maxLength: 15,
            pattern: '^([a-zA-Z0-9가-힣]){1,15}$',
            required: true,
            watch: true,
          })}
          disabled={isSubmitting}
          nickname={values['nickname']}
          {...args}
        />
      </form>
    </div>
  );
};

export const Default = Template.bind({});
