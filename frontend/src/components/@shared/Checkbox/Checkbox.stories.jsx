import { css } from '@emotion/react';
import Checkbox from '.';

export default {
  title: 'Components/Checkbox',
  component: Checkbox,
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
      <Checkbox {...args}>unchecked</Checkbox>
      <Checkbox {...args} defaultChecked={true}>
        checked
      </Checkbox>
    </div>
  );
};

export const Default = Template.bind({});

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
