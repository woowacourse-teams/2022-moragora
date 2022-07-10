import { css } from '@emotion/react';
import DivideLine from '.';

export default {
  title: 'Components/DivideLine',
  component: DivideLine,
};

const Template = (args) => {
  return (
    <DivideLine
      {...args}
      css={css`
        margin: 1rem;
      `}
    />
  );
};

export const Default = Template.bind({});
Default.args = {};
