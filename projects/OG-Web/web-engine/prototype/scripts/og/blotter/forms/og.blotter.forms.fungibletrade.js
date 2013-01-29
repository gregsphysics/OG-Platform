/**
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.blotter.forms.fungibletrade',
    dependencies: [],
    obj: function () {   
        return function (config) {
            var constructor = this, form, request, data, security, $security_input, util = og.blotter.util, 
            dropdown = '.og-blotter-security-select', bond_future = 'BondFutureSecurity',
            bond_arr = ['CorporateBondSecurity', 'GovernmentBondSecurity','MunicipalBondSecurity'], 
            details_selector = 'og-blocks-fungible-details', ids_selector = 'og-blocks-fungible-security-ids',
            blank_details = "<table class='" + details_selector + "'></table>",
            blank_ids = "<table class='" + ids_selector + "'></table>",
            type_map = {BOND: 1, BOND_FUT: 2, EXCHANGE_TRADED: 3};
            if(config) {data = config; data.id = config.trade.uniqueId;}
            else {data = {trade: og.blotter.util.fungible_trade};}
            constructor.load = function () {
                constructor.title = 'Fungible Trade';
                form = new og.common.util.ui.Form({
                    module: 'og.blotter.forms.fungible_tash',
                    data: data,
                    selector: '.OG-blotter-form-block'
                });
                security = new og.blotter.forms.blocks.Security({form: form, label: "Underlying ID", 
                    security: data.trade.securityIdBundle, index: "trade.securityIdBundle"});
                form.children.push(
                    new og.blotter.forms.blocks.Portfolio({form: form, counterparty: data.trade.counterparty}),
                    new form.Block({module: 'og.blotter.forms.blocks.fungible_tash', 
                        extras: {quantity: data.trade.quantity},
                        children: [security]
                    }),
                    new og.common.util.ui.Attributes({
                        form: form, attributes: data.trade.attributes, index: 'trade.attributes'
                    }),                    
                    new form.Block({content: blank_details}),                   
                    new form.Block({content: blank_ids})
                );
                form.dom();
                form.on('form:load', function () {get_security();});
                form.on('form:submit', function (result) {og.api.rest.blotter.trades.put(result.data);});
                form.on('keyup', security.input_id(), function (event) {get_security();});
                form.on('change', security.select_id(), function (event) {get_security();});
            }; 
            get_security = function () {
                request = og.api.rest.blotter.securities.get({id:security.name()}).pipe(
                    function(result){poplate_switch(result);}
                );
            };
            populate = function (config){
                var details_block, ids_block, details;
                /*switch(config.type){
                    case type_map.BOND:
                        details_block = new form.Block({module: 'og.blotter.forms.blocks.fungible_bond_tash',
                            extras:{issuer: config.data.issuerName, coupon_type: config.data.couponType, 
                            coupon_rate: config.data.couponRate, currency: config.data.currency}
                        });
                        break;
                    case type_map.BOND_FUT:
                        details_block = new form.Block({module: 'og.blotter.forms.blocks.fungible_bond_future_tash',
                            extras:{}
                        });
                        break;
                    case type_map.EXCHANGE_TRADED:
                        details_block = new form.Block({module: 'og.blotter.forms.blocks.fungible_exchange_traded_tash',
                            extras:{}
                        });
                        break;
                }*/
                ids_block = new form.Block({module: 'og.blotter.forms.blocks.fungible_security_ids_tash', 
                    extras: {security: security_ids(config.data.externalIdBundle)}
                });
                delete config.data.externalIdBundle;
                delete config.data.attributes;
                details = Object.keys(config.data).map(function(key) {
                    return {key: gentrify(key), value:config.data[key]};});
                details_block = new form.Block({module: 'og.blotter.forms.blocks.fungible_details_tash',
                    extras:{detail: details}
                });

                ids_block.html(function (html){
                    $('.' + ids_selector).replaceWith(html);
                });
                details_block.html(function (html){
                    $('.' + details_selector).replaceWith(html);
                });
            };
            gentrify = function (str){
                return str.replace(/([A-Z])/g, ' $1').replace(/^./, function(str){ return str.toUpperCase();});
            };
            poplate_switch = function (config){
                if(config.error) {clear_info(); return;}
                populate({data: config.data});
                /*if(~bond_arr.indexOf(config.data.type)) populate({type: type_map.BOND, data: config.data});
                else if(~bond_future.indexOf(config.data.type)) populate({type: type_map.BOND_FUT, data: config.data});
                else populate({type: type_map.EXCHANGE_TRADED, data: config.data});*/
            };
            security_ids = function (str) {
                return str.split(',').reduce(function(acc, val) {
                    var pair = val.split('~');
                    return acc.concat({'scheme': pair[0].trim(),'id': pair[1].trim()});
                }, []);
            };
            clear_info = function (){
                $('.' + details_selector).replaceWith(blank_details);
                $('.' + ids_selector).replaceWith(blank_ids);
            };
            constructor.load();
            constructor.submit = function () {
                form.submit();
            };
            constructor.submit_new = function () {
                delete data.id;
                form.submit();
            };
            constructor.kill = function () {
            };
        };
    }
});