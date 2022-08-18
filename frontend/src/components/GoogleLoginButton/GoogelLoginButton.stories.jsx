import { css } from '@emotion/react';
import GoogleLoginButton from '.';

export default {
  title: 'Components/GoogleLoginButton',
  component: GoogleLoginButton,
};

const Template = (args) => {
  return (
    <div
      css={css`
        width: 20rem;
      `}
    >
      <GoogleLoginButton {...args} />
    </div>
  );
};

export const Default = Template.bind({});
