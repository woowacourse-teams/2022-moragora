import { css } from '@emotion/react';
import RadioButton from '.';

export default {
  title: 'Components/RadioButton',
  component: RadioButton,
};

const Template = (args) => {
  return (
    <div
      css={css`
        display: flex;
        gap: 1rem;
      `}
    >
      <RadioButton {...args} defaultChecked />
      <RadioButton {...args} />
    </div>
  );
};

export const Default = Template.bind({});
Default.args = { name: 'radio' };

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
