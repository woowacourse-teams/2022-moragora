import { css } from '@emotion/react';
import CoffeeStackItem from '.';

export default {
  title: 'Components/CoffeeStackItem',
  component: CoffeeStackItem,
};

const Template = (args) => {
  return <CoffeeStackItem {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  name: '모임 1',
};
