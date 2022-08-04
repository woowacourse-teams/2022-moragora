import { css } from '@emotion/react';
import Toggle from '.';

export default {
  title: 'Components/Toggle',
  component: Toggle,
};

const Template = (args) => {
  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        gap: 1rem;
      `}
    >
      <Toggle {...args}>off</Toggle>
      <Toggle {...args} defaultChecked={true}>
        on
      </Toggle>
    </div>
  );
};

export const Default = Template.bind({});

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
